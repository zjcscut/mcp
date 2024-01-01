package cn.vlts.mcp.spi;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.Statement;
import java.util.*;

/**
 * MCP核心拦截器
 *
 * @author throwable
 * @version v1
 * @description MCP核心拦截器
 * @since 2023/12/19 10:00
 */
@SuppressWarnings("rawtypes")
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class}),
        @Signature(type = StatementHandler.class, method = "getBoundSql", args = {}),
        @Signature(type = StatementHandler.class, method = "parameterize", args = {Statement.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
public class McpCoreInterceptor implements Interceptor {

    private final List<InternalInterceptor> interceptors = new ArrayList<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();
        Object[] args = invocation.getArgs();
        // Executor类型
        if (target instanceof Executor) {
            Executor executor = (Executor) target;
            Object parameter = args[1];
            boolean isUpdate = args.length == 2;
            MappedStatement ms = (MappedStatement) args[0];
            if (!isUpdate && ms.getSqlCommandType() == SqlCommandType.SELECT) {
                RowBounds rowBounds = (RowBounds) args[2];
                ResultHandler resultHandler = (ResultHandler) args[3];
                BoundSql boundSql;
                if (args.length == 4) {
                    boundSql = ms.getBoundSql(parameter);
                } else {
                    // 几乎不可能走进这里面,除非使用Executor的代理对象调用query[args[6]]
                    boundSql = (BoundSql) args[5];
                }
                for (InternalInterceptor queryInterceptor : interceptors) {
                    if (!queryInterceptor.willDoQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql)) {
                        return Collections.emptyList();
                    }
                    queryInterceptor.beforeQuery(executor, ms, parameter, rowBounds, resultHandler, boundSql);
                }
                Object processResult = invocation.proceed();
                for (InternalInterceptor queryInterceptor : interceptors) {
                    processResult = queryInterceptor.afterQuery(executor, ms, parameter, rowBounds, resultHandler,
                            boundSql, processResult);
                }
                return processResult;
            } else if (isUpdate) {
                for (InternalInterceptor updateInterceptor : interceptors) {
                    if (!updateInterceptor.willDoUpdate(executor, ms, parameter)) {
                        return -1;
                    }
                    updateInterceptor.beforeUpdate(executor, ms, parameter);
                }
                Object processResult = invocation.proceed();
                for (InternalInterceptor updateInterceptor : interceptors) {
                    processResult = updateInterceptor.afterUpdate(executor, ms, parameter, processResult);
                }
                return processResult;
            }
        } else if (target instanceof StatementHandler) {
            // StatementHandler类型
            final StatementHandler sh = (StatementHandler) target;
            // 目前只有StatementHandler.getBoundSql方法args才为null
            if (null == args) {
                for (InternalInterceptor interceptor : interceptors) {
                    interceptor.beforeGetBoundSql(sh);
                }
            } else if (args.length == 1) {
                Statement statement = (Statement) args[0];
                for (InternalInterceptor interceptor : interceptors) {
                    if (interceptor.enableForceParameterize(sh, statement)) {
                        interceptor.forceParameterize(sh, statement);
                        return null;
                    }
                }
                for (InternalInterceptor interceptor : interceptors) {
                    interceptor.beforeParameterize(sh, statement);
                }
            } else {
                Connection connections = (Connection) args[0];
                Integer transactionTimeout = (Integer) args[1];
                for (InternalInterceptor interceptor : interceptors) {
                    interceptor.beforePrepare(sh, connections, transactionTimeout);
                }
            }
        } else if (target instanceof ResultSetHandler) {
            ResultSetHandler rsh = (ResultSetHandler) target;
            Statement statement = (Statement) args[0];
            for (InternalInterceptor interceptor : interceptors) {
                if (interceptor.enableForceHandleResultSets(rsh, statement)) {
                    return interceptor.forceHandleResultSets(rsh, statement);
                }
            }
            for (InternalInterceptor interceptor : interceptors) {
                interceptor.beforeHandleResultSets(rsh, statement);
            }
            Object processResult = invocation.proceed();
            for (InternalInterceptor interceptor : interceptors) {
                processResult = interceptor.afterHandleResultSets(rsh, statement, processResult);
            }
            return processResult;
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor || target instanceof StatementHandler || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        interceptors.forEach(internalInterceptor -> internalInterceptor.setProperties(properties));
    }

    public void addInnerInterceptor(InternalInterceptor innerInterceptor) {
        this.interceptors.add(innerInterceptor);
        this.interceptors.sort(Comparator.comparing(InternalInterceptor::order));
    }

    public List<InternalInterceptor> getInterceptors() {
        return Collections.unmodifiableList(interceptors);
    }
}
