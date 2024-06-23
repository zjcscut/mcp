# MCP

`MCP`（`MyBatis Crypto Plugin`）是一个基于`MyBatis`插件进行自定义扩展的实时加解密（支持灵活的加解密配置）组件，具备以下功能：

- 全局配置加解密方案
- 支持参数注入模式（实验性功能，直接通过修改`MyBatis`源码注入`TypeHandler`包装器）和反射模式（默认模式）
- 外置加解密规则配置
- 声明式（注解）加解密配置
- 内置加解密算法
- 自定义加解密处理器
- 加解密依赖的秘钥、向量等属性可以通过占位符通过`Spring Environment`获取
- 预加载包路径集合下的声明式加解密实体
- 快速失败

## 使用方式

`SpringBoot`体系中引入依赖：

```xml

<dependencies>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>${mybatis.version}</version>
    </dependency>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>
        <version>${mybatis.spring.version}</version>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-jdbc</artifactId>
    </dependency>
    <dependency>
        <groupId>cn.vlts</groupId>
        <artifactId>spring-boot-starter-mcp</artifactId>
        <version>${mcp.version}</version>
    </dependency>
</dependencies>
```

**非`SpringBoot`体系**中引入依赖：

```xml

<dependencies>
    <dependency>
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>
        <version>${mybatis.version}</version>
    </dependency>
    <dependency>
        <groupId>cn.vlts</groupId>
        <artifactId>mcp-core</artifactId>
        <version>${mcp.version}</version>
    </dependency>
</dependencies>
```

> `MCP`的最新版本可以从
> [`Maven`中央仓库](https://central.sonatype.com/artifact/cn.vlts/mcp)
> 或者[mvnrepository.com](https://mvnrepository.com/artifact/cn.vlts/mcp)查询

`MCP`底层基于`MyBatis`插件进行实现，核心拦截器是`McpCoreInterceptor`。如果使用`SqlSessionFactoryBean`
创建`SqlSessionFactory`需要手动设置插件`SqlSessionFactoryBean#setPlugins([McpCoreInterceptor])`

使用的例子：

- 非`SpringBoot`项目：[mcp-example](mcp-example)
- `SpringBoot`项目：[mcp-spring-boot-example](mcp-spring-boot-example)

## 配置

### 全局配置

全局配置属性如下：

```properties
## 是否启用插件
mcp.enabled=true
## 是否支持快速失败，快速失败模式下只要加解密出现异常马上把异常抛出
mcp.fast-fail=true
## 模式：REFLECTION - 反射模式（默认） || PARAM_INJECT - 参数注入模式（实验性功能）
mcp.mode=REFLECTION
## 全局加解密算法
mcp.global-crypto-algorithm=AES_ECB_PKCS5PADDING_BASE64
## 全局对称加解密密钥
mcp.global-key=1234567890123456
## 全局对称加解密向量
mcp.global-iv=1234567890123456
## 全局非对称加解密公钥
mcp.global-pub-key=
## 全局非对称加解密私钥
mcp.global-pri-key=
## 需要预加载的使用@CryptoField声明的实体类所在包路径集合
mcp.type-packages[0]=a.b
mcp.type-packages[1]=x.y
## 全局加解密处理器
mcp.global-crypto-processor=x.y.FooProcessor
```

### 声明式注解

可以使用`@CryptoField`注解声明实体类成员变量去定义加解密规则，该注解属性如下：

```java
public @interface CryptoField {

    /**
     * 对称加解密KEY，支持从Spring Environment获取
     *
     * @return key
     */
    String key() default "";

    /**
     * 对称加解密IV，支持从Spring Environment获取
     *
     * @return iv
     */
    String iv() default "";

    /**
     * 非对称加密Public Key，支持从Spring Environment获取
     *
     * @return public key
     */
    String pubKey() default "";

    /**
     * 非对称加密Private Key，支持从Spring Environment获取
     *
     * @return private key
     */
    String priKey() default "";

    /**
     * 内置加解密算法
     */
    CryptoAlgorithm algorithm() default CryptoAlgorithm.RAW;

    /**
     * 字符串加解密处理器
     */
    Class<? extends DuplexStringCryptoProcessor> cryptoProcessor() default DuplexStringCryptoProcessor.class;
}
```

### 外置配置

`MCP`会在容器启动时异步扫描`classpath`下`META-INF/mcp`目录下的所有`.json`后缀文件加载到内存中作为加解密配置缓存。
外置`JSON`配置文件格式：

```json
[
  {
    "className": "需要加解密的目标类全类名（反射模式下应用）",
    "msIdList": [
      "Mybatis MappedStatement ID列表（参数注入模式下应用）",
      "通过Mapper文件的namespace拼接CRUD的id组成:",
      "cn.vlts.example.repository.mapper.CustomerMapper.insertSelective"
    ],
    "rsmIdList": [
      "Mybatis ResultMap ID列表（参数注入模式下应用）",
      "特殊情况像通过resultType指定会生成如下特定的resultMapId:",
      "cn.vlts.example.repository.CustomerDao.selectOneById-Inline"
    ],
    "fields": [
      {
        "name": "成员变量名称",
        "key": "对称加解密密钥,支持${}参数从Spring Environment取值",
        "iv": "对称加解密向量,支持${}参数从Spring Environment取值",
        "pubKey": "非对称加解密共钥,支持${}参数从Spring Environment取值",
        "priKey": "非对称加解密私钥,支持${}参数从Spring Environment取值",
        "algorithm": "内置加解密算法实现,见CryptoAlgorithm",
        "cryptoProcessor": "自定义加解密处理器全类名,必须是DuplexStringCryptoProcessor的子类"
      }
    ]
  }
]
```

- 注意：外置加解密配置加载的类成员变量必须为`String`类型

## 内置加解密算法

内置加解密算法由枚举类`CryptoAlgorithm`定义，包含了常规的对称加解密算法实现和`RSA`加解密实现，见下表：

| 算法                               | 模式                        | 描述                          |
|----------------------------------|---------------------------|-----------------------------|
| `RAW`                            | `RAW`                     | 原始处理，不进行加解密处理               | 
| `AES_ECB_PKCS5PADDING_HEX`       | `AES/ECB/PKCS5Padding`    | -                           | 
| `AES_ECB_PKCS5PADDING_BASE64`    | `AES/ECB/PKCS5Padding`    | -                           | 
| `AES_CBC_PKCS5PADDING_HEX`       | `AES/CBC/PKCS5Padding`    | -                           | 
| `AES_CBC_PKCS5PADDING_BASE64`    | `AES/CBC/PKCS5Padding`    | -                           | 
| `DES_ECB_PKCS5PADDING_HEX`       | `DES/ECB/PKCS5Padding`    | -                           | 
| `DES_ECB_PKCS5PADDING_BASE64`    | `DES/ECB/PKCS5Padding`    | -                           | 
| `DES_CBC_PKCS5PADDING_HEX`       | `DES/CBC/PKCS5Padding`    | -                           | 
| `DEC_CBC_PKCS5PADDING_BASE64`    | `DES/CBC/PKCS5Padding`    | -                           | 
| `DESEDE_ECB_PKCS5PADDING_HEX`    | `DESede/ECB/PKCS5Padding` | `3DES`实现                    | 
| `DESEDE_ECB_PKCS5PADDING_BASE64` | `DESede/ECB/PKCS5Padding` | `3DES`实现                    | 
| `DESEDE_CBC_PKCS5PADDING_HEX`    | `DESede/CBC/PKCS5Padding` | `3DES`实现                    | 
| `DESEDE_CBC_PKCS5PADDING_BASE64` | `DESede/CBC/PKCS5Padding` | `3DES`实现                    | 
| `RSA_HEX`                        | `RSA`                     | 隐含模式`RSA/None/PKCS1Padding` | 
| `RSA_BASE64`                     | `RSA`                     | 隐含模式`RSA/None/PKCS1Padding` | 
| `RSA_ECB_PKCS1PADDING_HEX`       | `RSA/ECB/PKCS1Padding`    | -                           | 
| `RSA_ECB_PKCS1PADDING_BASE64`    | `RSA/ECB/PKCS1Padding`    | -                           | 

其中：

- `_HEX`后缀的为加密后结果基于十六进制编码
- `_BASE64`后缀的为加密后结果基于`BASE64`编码

## 扩展

### 自定义反射扩展

`MCP`在处理引用类型属性值或者和设置的时候允许使用外部反射扩展实现，使用`SPI`进行加载，该扩展由`ExternalReflectionProvider`
定义。例如新建一个文件`META-INF/services/cn.vlts.mcp.util.ExternalReflectionProvider`：

```shell
cn.vlts.example.util.CustomReflectionProvider
```

实现类`CustomReflectionProvider`如下：

```java
public class CustomReflectionProvider implements ExternalReflectionProvider {

    @Override
    public void doWithFields(Class<?> clazz, McpReflectionUtils.FieldCallback fc, McpReflectionUtils.FieldFilter ff) {
        org.springframework.util.ReflectionUtils.doWithFields(clazz, fc::doWith, ff::matches);
    }

    @Override
    public void doWithMethods(Class<?> clazz, McpReflectionUtils.MethodCallback mc, McpReflectionUtils.MethodFilter mf) {
        org.springframework.util.ReflectionUtils.doWithMethods(clazz, mc::doWith, mf::matches);
    }
}
```

这样可以通过外置的反射扩展模块优化引用类型反射获取和设置值的性能。 在使用`spring-boot-starter-mcp`
的情况下，默认会启用`SpringExternalReflectionProvider`。

### 自定义属性配置器

属性配置器`CryptoConfigConfigurer`用于构建加解密处理器之前对于加解密配置进行获取或者修改。例如：

```java

@Slf4j
@Component
public class CustomCryptoConfigConfigurer implements CryptoConfigConfigurer {

    @Override
    public void apply(CryptoTarget cryptoTarget, CryptoConfig config) {
        if (cryptoTarget.isJavaField()) {
            if (Objects.equals(cryptoTarget.fieldName(), "foo")) {
                config.setKey("1111111122222222");
            }
        }
    }
}
```

在使用`spring-boot-starter-mcp`的情况下，默认会启用`SpEvnCryptoConfigConfigurer`。

### 自定义加解密处理器

可以自行实现`DuplexStringCryptoProcessor`接口创建自定义加解密处理器。例如：

```java
public class CustomCryptoProcessor implements DuplexStringCryptoProcessor {

    @Override
    public void init(String alg, CryptoConfig config) {
        // 初始化逻辑
    }

    @Override
    public String processDecryption(String content) {
        // 字符串加密逻辑
    }

    @Override
    public String processEncryption(String content) {
        // 字符串解密逻辑
    }
}
```

可以在外部配置或者声明式注解中通过全类名指定使用该自定义加解密处理器。

## 说明

### 加解密配置优先级

通过`Spring Environment`加载的配置 > 注解声明式中自定义的配置 > 全局配置

### 加解密处理器加载优先级

通过外置配置文件加载的加解密处理器 > 注解声明式中自定义加解密处理器 > 注解声明式中内置加解密处理器 > 全局加解密处理器 >
原始加解密处理器（关闭快速失败模式）

## LICENSE

This code is distributed under the MIT License. See **LICENSE** in this directory.
