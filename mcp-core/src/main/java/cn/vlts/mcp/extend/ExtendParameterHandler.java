package cn.vlts.mcp.extend;


import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;
import cn.vlts.mcp.util.MultiKey;
import cn.vlts.mcp.util.MybatisUtils;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 扩展Mybatis参数处理器
 *
 * @author throwable
 * @version v1
 * @description 扩展Mybatis参数处理器
 * @since 2023/12/25 15:01
 */
public class ExtendParameterHandler implements ParameterHandler {

    private static final String TYPE_HANDLER_REGISTRY_FIELD_KEY = "typeHandlerRegistry";

    private static final String MAPPED_STATEMENT_FIELD_KEY = "mappedStatement";

    private static final String BOUND_SQL_FIELD_KEY = "boundSql";

    private final DefaultParameterHandler handler;

    private final TypeHandlerRegistry typeHandlerRegistry;

    private final MappedStatement mappedStatement;

    private final BoundSql boundSql;

    private final Configuration configuration;

    private final Function<MultiKey, DuplexStringCryptoProcessor> cryptoProcessorLoader;

    private ExtendParameterHandler(StatementHandler statementHandler,
                                   Function<MultiKey, DuplexStringCryptoProcessor> cryptoProcessorLoader) {
        this.handler = (DefaultParameterHandler) statementHandler.getParameterHandler();
        MetaObject metaObject = MybatisUtils.X.newMetaObject(this.handler);
        this.typeHandlerRegistry = (TypeHandlerRegistry) metaObject.getValue(TYPE_HANDLER_REGISTRY_FIELD_KEY);
        this.mappedStatement = (MappedStatement) metaObject.getValue(MAPPED_STATEMENT_FIELD_KEY);
        this.boundSql = (BoundSql) metaObject.getValue(BOUND_SQL_FIELD_KEY);
        this.configuration = this.mappedStatement.getConfiguration();
        this.cryptoProcessorLoader = cryptoProcessorLoader;
    }

    public static ExtendParameterHandler newInstance(StatementHandler statementHandler,
                                                     Function<MultiKey, DuplexStringCryptoProcessor> cryptoProcessorLoader) {
        return new ExtendParameterHandler(statementHandler, cryptoProcessorLoader);
    }

    @Override
    public Object getParameterObject() {
        return handler.getParameterObject();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void setParameters(PreparedStatement ps) throws SQLException {
        Object parameterObject = getParameterObject();
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            MetaObject metaObject = null;
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (Objects.isNull(parameterObject)) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        if (Objects.isNull(metaObject)) {
                            metaObject = configuration.newMetaObject(parameterObject);
                        }
                        value = metaObject.getValue(propertyName);
                    }
                    TypeHandler typeHandler = null;
                    if (Objects.equals(String.class, parameterMapping.getJavaType()) && Objects.nonNull(value)) {
                        MultiKey multiKey = MultiKey.newInstance(mappedStatement.getId(), propertyName);
                        DuplexStringCryptoProcessor cryptoProcessor = cryptoProcessorLoader.apply(multiKey);
                        if (Objects.nonNull(cryptoProcessor)) {
                            typeHandler = CryptoSupportTypeHandler.newInstance(cryptoProcessor);
                        }
                    }
                    if (Objects.isNull(typeHandler)) {
                        typeHandler = parameterMapping.getTypeHandler();
                    }
                    JdbcType jdbcType = parameterMapping.getJdbcType();
                    if (Objects.isNull(value) && Objects.isNull(jdbcType)) {
                        jdbcType = configuration.getJdbcTypeForNull();
                    }
                    try {
                        typeHandler.setParameter(ps, i + 1, value, jdbcType);
                    } catch (TypeException | SQLException e) {
                        throw new TypeException("Could not set parameters for mapping: " + parameterMapping + ". Cause: " + e, e);
                    }
                }
            }
        }
    }
}
