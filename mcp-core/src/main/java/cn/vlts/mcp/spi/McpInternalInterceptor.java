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

package cn.vlts.mcp.spi;


import cn.vlts.mcp.common.CryptoAlgorithm;
import cn.vlts.mcp.common.Mode;
import cn.vlts.mcp.config.McpConfig;
import cn.vlts.mcp.crypto.*;
import cn.vlts.mcp.exception.McpException;
import cn.vlts.mcp.extend.ExtendParameterHandler;
import cn.vlts.mcp.extend.ExtendResultSetHandler;
import cn.vlts.mcp.util.McpClassUtils;
import cn.vlts.mcp.util.McpReflectionUtils;
import cn.vlts.mcp.util.McpStringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * MCP内部拦截器
 *
 * @author throwable
 * @version v1
 * @description MCP内部拦截器
 * @since 2023/12/20 15:21
 */
public class McpInternalInterceptor implements InternalInterceptor, FieldCryptoProcessorChooser, GlobalConfigProvider {

    private final McpConfig mcpConfig;

    private final List<CryptoConfigConfigurer> cryptoConfigConfigurerList = new ArrayList<>();

    private final CryptoProcessorFactory cryptoProcessorFactory;

    private final FieldCryptoProcessorRegistry fieldCryptoProcessorRegistry;

    private final ReferenceCryptoMatcher referenceCryptoMatcher;

    private final boolean enabled;

    private final Mode mode;

    private final boolean fastFail;

    private CryptoConfig globalCryptoConfig = null;

    private DuplexStringCryptoProcessor globalCryptoProcessor = null;

    private final ConcurrentMap<Class<?>, List<Field>> refTypeFieldCache = new ConcurrentHashMap<>();

    public McpInternalInterceptor(McpConfig mcpConfig,
                                  CryptoProcessorFactory cryptoProcessorFactory,
                                  FieldCryptoProcessorRegistry fieldCryptoProcessorRegistry,
                                  List<CryptoConfigConfigurer> cryptoConfigConfigurerList,
                                  ReferenceCryptoMatcher referenceCryptoMatcher) {
        this.mcpConfig = mcpConfig;
        this.cryptoProcessorFactory = cryptoProcessorFactory;
        this.fieldCryptoProcessorRegistry = fieldCryptoProcessorRegistry;
        this.referenceCryptoMatcher = referenceCryptoMatcher;
        this.enabled = Objects.equals(mcpConfig.getEnabled(), Boolean.TRUE);
        this.fastFail = Objects.equals(mcpConfig.getFastFail(), Boolean.TRUE);
        this.mode = Optional.ofNullable(mcpConfig.getMode()).orElse(Mode.REFLECTION);
        if (Objects.nonNull(cryptoConfigConfigurerList) && !cryptoConfigConfigurerList.isEmpty()) {
            this.cryptoConfigConfigurerList.addAll(cryptoConfigConfigurerList);
            this.cryptoConfigConfigurerList.sort(Comparator.comparing(CryptoConfigConfigurer::order));
        }
        init();
    }

    public McpInternalInterceptor(McpConfig mcpConfig) {
        this(mcpConfig, new DefaultCryptoProcessorFactory(), new DefaultFieldCryptoProcessorRegistry(),
                new ArrayList<>(), ReferenceCryptoMatcher.DEFAULT);
    }

    private void init() {
        globalCryptoConfig = new CryptoConfig();
        globalCryptoConfig.setKey(mcpConfig.getGlobalKey());
        globalCryptoConfig.setIv(mcpConfig.getGlobalIv());
        globalCryptoConfig.setPubKey(mcpConfig.getGlobalPubKey());
        globalCryptoConfig.setPriKey(mcpConfig.getGlobalPriKey());
        Class<? extends DuplexStringCryptoProcessor> cryptoProcessorType = mcpConfig.getGlobalCryptoProcessor();
        if (Objects.nonNull(cryptoProcessorType)) {
            globalCryptoProcessor = cryptoProcessorFactory.loadCustomCryptoProcessor(cryptoProcessorType, globalCryptoConfig);
        } else if (McpStringUtils.X.isNotBlank(mcpConfig.getGlobalCryptoAlgorithm())) {
            CryptoAlgorithm cryptoAlgorithm = CryptoAlgorithm.fromCryptoAlgorithm(mcpConfig.getGlobalCryptoAlgorithm());
            if (Objects.nonNull(cryptoAlgorithm)) {
                globalCryptoProcessor = cryptoProcessorFactory.loadBuildInCryptoProcessor(cryptoAlgorithm, globalCryptoConfig);
            }
        }
    }

    @Override
    public boolean enableForceHandleResultSets(ResultSetHandler rsh, Statement statement) throws SQLException {
        return enabled && Mode.PARAM_INJECT == mode;
    }

    @Override
    public Object forceHandleResultSets(ResultSetHandler rsh, Statement statement) throws SQLException {
        return ExtendResultSetHandler.newInstance(rsh, fieldCryptoProcessorRegistry::getKeysCryptoProcessor)
                .handleResultSets(statement);
    }

    @Override
    public boolean enableForceParameterize(StatementHandler sh, Statement statement) throws SQLException {
        return enabled && Mode.PARAM_INJECT == mode;
    }

    @Override
    public void forceParameterize(StatementHandler sh, Statement statement) throws SQLException {
        ExtendParameterHandler.newInstance(sh, fieldCryptoProcessorRegistry::getKeysCryptoProcessor)
                .setParameters((PreparedStatement) statement);
    }

    @Override
    public void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        // 反射模式下 - 处理引用类型加密
        if (enabled && Mode.REFLECTION == mode) {
            processReferenceCrypto(parameter, true);
        }
    }

    @Override
    public Object afterHandleResultSets(ResultSetHandler rsh, Statement statement, Object processResult) throws SQLException {
        // 反射模式下 - 处理引用类型解密
        if (enabled && Mode.REFLECTION == mode) {
            processReferenceCrypto(processResult, false);
        }
        return processResult;
    }

    private void processReferenceCrypto(Object ref, boolean encryptMode) {
        if (Objects.isNull(ref)) {
            return;
        }
        if (ref instanceof Collection) {
            Collection<?> collection = (Collection<?>) ref;
            if (!collection.isEmpty()) {
                Class<?> firstItemType = collection
                        .stream()
                        .filter(Objects::nonNull).findFirst()
                        .map(Object::getClass)
                        .orElse(null);
                if (Objects.nonNull(firstItemType) && McpClassUtils.isUserDefinedType(firstItemType)) {
                    for (Object entity : collection) {
                        processReferenceFieldCrypto(firstItemType, entity, encryptMode);
                    }
                }
            }
        } else {
            Class<?> type = ref.getClass();
            if (McpClassUtils.isUserDefinedType(type)) {
                processReferenceFieldCrypto(type, ref, encryptMode);
            }
        }
    }

    private void processReferenceFieldCrypto(Class<?> type, Object ref, boolean encryptMode) {
        if (!referenceCryptoMatcher.match(type, ref, encryptMode)) {
            return;
        }
        refTypeFieldCache.computeIfAbsent(type, refType -> {
            List<Field> fieldList = new ArrayList<>();
            McpReflectionUtils.doWithFields(refType, fieldList::add,
                    field -> field.isAnnotationPresent(CryptoField.class) ||
                            fieldCryptoProcessorRegistry.existFieldCryptoProcessor(field));
            return fieldList;
        }).forEach(field -> {
            Object originalVal = McpReflectionUtils.getFieldValue(ref, field);
            if (Objects.nonNull(originalVal)) {
                // 集合
                if (originalVal instanceof Collection) {
                    Collection<?> collection = (Collection<?>) originalVal;
                    if (!collection.isEmpty()) {
                        for (Object elem : collection) {
                            processReferenceCrypto(elem, encryptMode);
                        }
                    }
                } else if (originalVal instanceof String) {
                    // 字符串
                    String stringVal = (String) originalVal;
                    DuplexStringCryptoProcessor cryptoProcessor = chooseFieldCryptoProcessor(field);
                    if (Objects.nonNull(cryptoProcessor)) {
                        String targetVal = stringVal;
                        try {
                            if (encryptMode) {
                                targetVal = cryptoProcessor.processBeforeEncryption(targetVal);
                                targetVal = cryptoProcessor.processEncryption(targetVal);
                                targetVal = cryptoProcessor.processAfterEncryption(targetVal);
                            } else {
                                targetVal = cryptoProcessor.processBeforeDecryption(targetVal);
                                targetVal = cryptoProcessor.processDecryption(targetVal);
                                targetVal = cryptoProcessor.processAfterDecryption(targetVal);
                            }
                        } catch (Exception e) {
                            // 快速失败
                            if (fastFail) {
                                throw new McpException(e);
                            }
                        }
                        McpReflectionUtils.setFieldValue(ref, field, targetVal);
                    }
                } else if (McpClassUtils.isUserDefinedType(field.getType())) {
                    // 自定义类型 - 当作引用类型处理
                    processReferenceFieldCrypto(field.getType(), originalVal, encryptMode);
                }
            }
        });
    }

    /**
     * 选取字段加解密处理器
     *
     * @param field field
     * @return crypto processor
     */
    @Override
    public DuplexStringCryptoProcessor chooseFieldCryptoProcessor(Field field) {
        // 1. 优先使用字段加解密处理器
        DuplexStringCryptoProcessor cryptoProcessor = fieldCryptoProcessorRegistry.getFieldCryptoProcessor(field);
        if (Objects.nonNull(cryptoProcessor)) {
            return cryptoProcessor;
        }
        CryptoField cryptoField = field.getAnnotation(CryptoField.class);
        if (Objects.nonNull(cryptoField)) {
            return fieldCryptoProcessorRegistry.registerFieldAnnotationCryptoProcessor(cryptoField, cf -> {
                // 2. 使用注解中自定义加解密处理器
                Class<? extends DuplexStringCryptoProcessor> cryptoProcessorType = cf.cryptoProcessor();
                if (Objects.nonNull(cryptoProcessorType) && !Objects.equals(DuplexStringCryptoProcessor.class, cryptoProcessorType)) {
                    CryptoConfig config = buildFieldCryptoConfig(field, cf);
                    DuplexStringCryptoProcessor customCryptoProcessor
                            = cryptoProcessorFactory.loadCustomCryptoProcessor(cryptoProcessorType, config);
                    customCryptoProcessor.init(cryptoProcessorType.getName(), config);
                    return customCryptoProcessor;
                }
                // 3. 使用内置的加解密处理器
                CryptoAlgorithm cryptoAlgorithm = cf.algorithm();
                if (Objects.nonNull(cryptoAlgorithm) && !Objects.equals(CryptoAlgorithm.RAW, cryptoAlgorithm)) {
                    CryptoConfig config = buildFieldCryptoConfig(field, cf);
                    DuplexStringCryptoProcessor buildInCryptoProcessor =
                            cryptoProcessorFactory.loadBuildInCryptoProcessor(cryptoAlgorithm, config);
                    buildInCryptoProcessor.init(cryptoAlgorithm.name(), config);
                    return buildInCryptoProcessor;
                }
                // 4. 使用全局的加解密处理器
                if (Objects.nonNull(globalCryptoProcessor)) {
                    return globalCryptoProcessor;
                }
                // 字段注解找不到加解密处理器 - 进行快速失败抛出异常
                if (fastFail) {
                    throw new McpException(String.format("字段%s.%s加载加解密处理器失败",
                            field.getDeclaringClass().getName(), field.getName()));
                }
                // 5. 不启用快速失败则返回一个兜底的不进行加解密的原始处理器
                return RawStringCryptoProcessor.INSTANCE;
            });
        }
        return null;
    }

    private CryptoConfig buildFieldCryptoConfig(Field field, CryptoField cryptoField) {
        CryptoConfig fieldConfig = new CryptoConfig();
        fieldConfig.setKey(cryptoField.key());
        fieldConfig.setIv(cryptoField.iv());
        fieldConfig.setPubKey(cryptoField.pubKey());
        fieldConfig.setPriKey(cryptoField.priKey());
        for (CryptoConfigConfigurer configurer : cryptoConfigConfigurerList) {
            if (configurer.match(field, cryptoField)) {
                configurer.apply(field, cryptoField, fieldConfig);
            }
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getKey())) {
            fieldConfig.setKey(globalCryptoConfig.getKey());
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getIv())) {
            fieldConfig.setIv(globalCryptoConfig.getIv());
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getPubKey())) {
            fieldConfig.setPubKey(globalCryptoConfig.getPubKey());
        }
        if (McpStringUtils.X.isBlank(fieldConfig.getPriKey())) {
            fieldConfig.setPriKey(globalCryptoConfig.getPriKey());
        }
        return fieldConfig;
    }

    @Override
    public String getGlobalKey() {
        return Optional.ofNullable(globalCryptoConfig).map(CryptoConfig::getKey).orElse(null);
    }

    @Override
    public String getGlobalIv() {
        return Optional.ofNullable(globalCryptoConfig).map(CryptoConfig::getIv).orElse(null);
    }

    @Override
    public String getGlobalPubKey() {
        return Optional.ofNullable(globalCryptoConfig).map(CryptoConfig::getPubKey).orElse(null);
    }

    @Override
    public String getGlobalPriKey() {
        return Optional.ofNullable(globalCryptoConfig).map(CryptoConfig::getPriKey).orElse(null);
    }

    @Override
    public DuplexStringCryptoProcessor getGlobalCryptoProcessor() {
        return globalCryptoProcessor;
    }
}
