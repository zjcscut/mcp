/*
 * MIT License
 *
 * Copyright (c) 2024 vlts.cn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package cn.vlts.mcp;


import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.crypto.*;
import cn.vlts.mcp.spi.CryptoField;
import cn.vlts.mcp.spi.CryptoTarget;
import cn.vlts.mcp.spi.GlobalConfigProvider;
import cn.vlts.mcp.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * MCP初始化器
 *
 * @author throwable
 * @version v1
 * @description MCP初始化器
 * @since 2023/12/21 11:28
 */
public class McpInitializer {

    private static final String EXTERNAL_CONFIG_FILE_DIR = "META-INF/mcp";

    private static final String EXTERNAL_CONFIG_FILE_SUFFIX = ".json";

    private static final Logger LOGGER = LoggerFactory.getLogger(McpInitializer.class);

    private static final ResourcePatternResolver RESOURCE_PATTERN_RESOLVER = new PathMatchingResourcePatternResolver();

    private static final CachingMetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();

    private final List<CryptoConfigConfigurer> cryptoConfigConfigurerList = new ArrayList<>();

    private final List<String> scanPackages;

    private final CryptoProcessorFactory cryptoProcessorFactory;

    private final FieldCryptoProcessorRegistry fieldCryptoProcessorRegistry;

    private final FieldCryptoProcessorChooser fieldCryptoProcessorChooser;

    private final GlobalConfigProvider globalConfigProvider;

    private final ExecutorService worker = Executors.newSingleThreadExecutor(task -> {
        Thread thread = new Thread(task, "mcp-init-worker");
        thread.setDaemon(true);
        return thread;
    });

    public McpInitializer(List<String> scanPackages,
                          CryptoProcessorFactory cryptoProcessorFactory,
                          FieldCryptoProcessorRegistry fieldCryptoProcessorRegistry,
                          FieldCryptoProcessorChooser fieldCryptoProcessorChooser,
                          GlobalConfigProvider globalConfigProvider,
                          List<CryptoConfigConfigurer> cryptoConfigConfigurerList) {
        this.scanPackages = scanPackages;
        this.cryptoProcessorFactory = cryptoProcessorFactory;
        this.fieldCryptoProcessorRegistry = fieldCryptoProcessorRegistry;
        this.fieldCryptoProcessorChooser = fieldCryptoProcessorChooser;
        this.globalConfigProvider = globalConfigProvider;
        if (Objects.nonNull(cryptoConfigConfigurerList) && !cryptoConfigConfigurerList.isEmpty()) {
            this.cryptoConfigConfigurerList.addAll(cryptoConfigConfigurerList);
            this.cryptoConfigConfigurerList.sort(Comparator.comparing(CryptoConfigConfigurer::order));
        }
    }

    /**
     * 异步初始化
     * 1. 加载和处理ExternalReflectionProvider（同步处理）
     * 2. 加载所有外部配置文件并且动态注册对应的字段加解密处理器
     * 3. 扫描所有需要加载的包路径下的实体，基于注解进行匹配和加载
     */
    public void initAsync() {
        initExternalReflectionProvider();
        worker.execute(() -> {
            StopWatch watch = new StopWatch();
            watch.start();
            loadExternalConfigFiles();
            scanEntities();
            watch.stop();
            long ms = watch.getTotalTimeMillis();
            LOGGER.info("MCP初始化完成,耗时:{} ms", ms);
        });
    }

    private void initExternalReflectionProvider() {
        Iterator<ExternalReflectionProvider> iterator = ServiceLoader.load(ExternalReflectionProvider.class).iterator();
        if (iterator.hasNext()) {
            McpReflectionUtils.attachExternalProvider(iterator.next());
        } else {
            McpReflectionUtils.attachExternalProvider(new SpExternalReflectionProvider());
        }
    }

    private void loadExternalConfigFiles() {
        List<ExternalConfigFileItem> fileItemList;
        try {
            fileItemList = doScanExternalConfigFiles();
        } catch (Exception e) {
            LOGGER.error("加载外部配置文件失败", e);
            return;
        }
        List<ExternalConfigFileItem> classConfigList = fileItemList
                .stream()
                .filter(item -> McpStringUtils.X.isNotBlank(item.getClassName()))
                .collect(Collectors.toList());
        List<ExternalConfigFileItem> msConfigList = fileItemList
                .stream()
                .filter(item -> !CollectionUtils.isEmpty(item.getMsIdList()))
                .collect(Collectors.toList());
        List<ExternalConfigFileItem> rsmConfigList = fileItemList
                .stream()
                .filter(item -> !CollectionUtils.isEmpty(item.getRsmIdList()))
                .collect(Collectors.toList());
        processClassConfigList(classConfigList);
        msConfigList.forEach(item -> processKeysConfigList(item.getFields(), item.getMsIdList(), true));
        rsmConfigList.forEach(item -> processKeysConfigList(item.getFields(), item.getRsmIdList(), false));
    }

    private void processClassConfigList(List<ExternalConfigFileItem> classConfigList) {
        for (ExternalConfigFileItem fileItem : classConfigList) {
            String className = fileItem.getClassName();
            if (McpStringUtils.X.isBlank(className)) {
                continue;
            }
            Class<?> clazz;
            try {
                clazz = ClassUtils.forName(className, null);
            } catch (Exception e) {
                LOGGER.error("初始化类{}失败", className, e);
                continue;
            }
            if (CollectionUtils.isEmpty(fileItem.getFields())) {
                continue;
            }
            for (ExternalConfigFieldItem fieldItem : fileItem.getFields()) {
                if (McpStringUtils.X.isNotBlank(fieldItem.getName())) {
                    Field field = McpReflectionUtils.findField(clazz, fieldItem.getName(), String.class);
                    if (Objects.nonNull(field)) {
                        CryptoConfig fieldConfig = new CryptoConfig();
                        fieldConfig.setKey(fieldItem.getKey());
                        fieldConfig.setIv(fieldItem.getIv());
                        fieldConfig.setPubKey(fieldItem.getPubKey());
                        fieldConfig.setPriKey(fieldItem.getPriKey());
                        CryptoTarget cryptoTarget = CryptoTarget.fromJavaField(field);
                        DuplexStringCryptoProcessor cryptoProcessor = buildCryptoTargetCryptoProcessor(cryptoTarget, fieldConfig,
                                fieldItem.getAlgorithm(), fieldItem.getCryptoProcessor());
                        if (Objects.nonNull(cryptoProcessor)) {
                            fieldCryptoProcessorRegistry.registerFieldCryptoProcessor(field, cryptoProcessor);
                        }
                    }
                }
            }
        }
    }

    private void processKeysConfigList(List<ExternalConfigFieldItem> fields, List<String> keys, boolean isMsId) {
        if (CollectionUtils.isEmpty(fields) || CollectionUtils.isEmpty(keys)) {
            return;
        }
        for (String key : keys) {
            for (ExternalConfigFieldItem fieldItem : fields) {
                String property = fieldItem.getName();
                if (McpStringUtils.X.isNotBlank(property)) {
                    CryptoConfig fieldConfig = new CryptoConfig();
                    fieldConfig.setKey(fieldItem.getKey());
                    fieldConfig.setIv(fieldItem.getIv());
                    fieldConfig.setPubKey(fieldItem.getPubKey());
                    fieldConfig.setPriKey(fieldItem.getPriKey());
                    CryptoTarget cryptoTarget;
                    if (isMsId) {
                        cryptoTarget = CryptoTarget.fromMsId(key, property);
                    } else {
                        cryptoTarget = CryptoTarget.fromRsmId(key, property);
                    }
                    DuplexStringCryptoProcessor cryptoProcessor = buildCryptoTargetCryptoProcessor(cryptoTarget, fieldConfig,
                            fieldItem.getAlgorithm(), fieldItem.getCryptoProcessor());
                    if (Objects.nonNull(cryptoProcessor)) {
                        fieldCryptoProcessorRegistry.registerKeysCryptoProcessor(MultiKey.newInstance(key, property), cryptoProcessor);
                    }
                }
            }
        }
    }

    private List<ExternalConfigFileItem> doScanExternalConfigFiles() throws IOException {
        List<ExternalConfigFileItem> fileItemSet = new ArrayList<>();
        Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + EXTERNAL_CONFIG_FILE_DIR + "/**/*" + EXTERNAL_CONFIG_FILE_SUFFIX);
        for (Resource resource : resources) {
            try {
                byte[] bytes = FileCopyUtils.copyToByteArray(resource.getFile());
                List<ExternalConfigFileItem> fileItem = McpJacksonUtils.X.parseList(bytes, ExternalConfigFileItem.class);
                if (Objects.nonNull(fileItem)) {
                    fileItemSet.addAll(fileItem);
                }
            } catch (Exception e) {
                LOGGER.warn("加载外部配置文件{}失败", resource.getFilename(), e);
            }
        }
        return fileItemSet;
    }

    @SuppressWarnings("unchecked")
    private DuplexStringCryptoProcessor buildCryptoTargetCryptoProcessor(CryptoTarget cryptoTarget,
                                                                         CryptoConfig fieldConfig,
                                                                         String algorithm,
                                                                         String cryptoProcessor) {
        try {
            if (McpStringUtils.X.isNotBlank(cryptoProcessor)) {
                Class<? extends DuplexStringCryptoProcessor> cryptoProcessorType =
                        (Class<? extends DuplexStringCryptoProcessor>) ClassUtils.forName(cryptoProcessor, null);
                if (!Objects.equals(DuplexStringCryptoProcessor.class, cryptoProcessorType)) {
                    CryptoConfig config = buildFieldCryptoConfig(cryptoTarget, fieldConfig);
                    DuplexStringCryptoProcessor customCryptoProcessor
                            = cryptoProcessorFactory.loadCustomCryptoProcessor(cryptoProcessorType, config);
                    customCryptoProcessor.init(cryptoProcessorType.getName(), config);
                    return customCryptoProcessor;
                }
            }
            CryptoAlgorithm cryptoAlgorithm = CryptoAlgorithm.fromCryptoAlgorithm(algorithm);
            if (Objects.nonNull(cryptoAlgorithm) && !Objects.equals(CryptoAlgorithm.RAW, cryptoAlgorithm)) {
                CryptoConfig config = buildFieldCryptoConfig(cryptoTarget, fieldConfig);
                DuplexStringCryptoProcessor buildInCryptoProcessor
                        = cryptoProcessorFactory.loadBuildInCryptoProcessor(cryptoAlgorithm, config);
                buildInCryptoProcessor.init(cryptoAlgorithm.name(), config);
                return buildInCryptoProcessor;
            }
            if (Objects.nonNull(globalConfigProvider.getGlobalCryptoProcessor())) {
                return globalConfigProvider.getGlobalCryptoProcessor();
            }
        } catch (Exception e) {
            if (Objects.nonNull(cryptoTarget)) {
                LOGGER.warn("加载外部配置字段加解密处理器失败,目标:{}", cryptoTarget.key(), e);
            } else {
                LOGGER.warn("加载外部配置字段加解密处理器失败", e);
            }
        }
        return null;
    }

    private CryptoConfig buildFieldCryptoConfig(CryptoTarget cryptoTarget, CryptoConfig fieldConfig) {
        for (CryptoConfigConfigurer configurer : cryptoConfigConfigurerList) {
            if (configurer.match(cryptoTarget)) {
                configurer.apply(cryptoTarget, fieldConfig);
            }
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getKey())) {
            fieldConfig.setKey(globalConfigProvider.getGlobalKey());
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getIv())) {
            fieldConfig.setIv(globalConfigProvider.getGlobalIv());
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getPubKey())) {
            fieldConfig.setPubKey(globalConfigProvider.getGlobalPubKey());
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getPriKey())) {
            fieldConfig.setPriKey(globalConfigProvider.getGlobalPriKey());
        }
        return fieldConfig;
    }

    private void scanEntities() {
        Set<Class<?>> classSet;
        try {
            classSet = doScanPackages();
        } catch (Exception e) {
            LOGGER.error("扫描@CryptoField实体失败", e);
            return;
        }
        classSet.stream()
                .filter(clazz -> !clazz.isAnonymousClass() && !clazz.isInterface() && !clazz.isMemberClass())
                .forEach(clazz -> McpReflectionUtils.doWithFields(clazz, fieldCryptoProcessorChooser::chooseFieldCryptoProcessor, field -> {
                    // 1. 必须是String类型
                    // 2. 存在@CryptoField注解 || 存在字段加解密处理器
                    return Objects.equals(field.getType(), String.class) &&
                            (field.isAnnotationPresent(CryptoField.class) || fieldCryptoProcessorRegistry.existFieldCryptoProcessor(field));
                }));
    }

    private Set<Class<?>> doScanPackages() throws IOException {
        Set<Class<?>> classSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(scanPackages)) {
            try {
                String[] packageArray = scanPackages.toArray(new String[0]);
                for (String packagePattern : packageArray) {
                    Resource[] resources = RESOURCE_PATTERN_RESOLVER.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                            + ClassUtils.convertClassNameToResourcePath(packagePattern) + "/**/*.class");
                    for (Resource resource : resources) {
                        try {
                            ClassMetadata classMetadata = METADATA_READER_FACTORY.getMetadataReader(resource).getClassMetadata();
                            Class<?> clazz = ClassUtils.forName(classMetadata.getClassName(), null);
                            classSet.add(clazz);
                        } catch (Exception e) {
                            LOGGER.warn("加载资源{}失败", resource.getFilename(), e);
                        }
                    }
                }
            } finally {
                METADATA_READER_FACTORY.clearCache();
            }
        }
        return classSet;
    }

    public void close() {
        worker.shutdown();
    }
}
