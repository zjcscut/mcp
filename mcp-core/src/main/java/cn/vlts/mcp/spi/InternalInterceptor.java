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

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ReuseExecutor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Properties;

/**
 * 内部拦截器 - 参考Mybatis-Plus的实现
 *
 * @author throwable
 * @version v1
 * @description 内部拦截器
 * @since 2023/12/19 10:01
 */
@SuppressWarnings({"rawtypes"})
public interface InternalInterceptor {

    /**
     * 判断是否执行 {@link Executor#query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)}
     * <p>
     * 如果不执行query操作,则返回 {@link Collections#emptyList()}
     *
     * @param executor      Executor(可能是代理对象)
     * @param ms            MappedStatement
     * @param parameter     parameter
     * @param rowBounds     rowBounds
     * @param resultHandler resultHandler
     * @param boundSql      boundSql
     * @return 新的 boundSql
     */
    default boolean willDoQuery(Executor executor, MappedStatement ms, Object parameter,
                                RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        return true;
    }

    /**
     * {@link Executor#query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)} 操作前置处理
     * <p>
     *
     * @param executor      Executor(可能是代理对象)
     * @param ms            MappedStatement
     * @param parameter     parameter
     * @param rowBounds     rowBounds
     * @param resultHandler resultHandler
     * @param boundSql      boundSql
     */
    default void beforeQuery(Executor executor, MappedStatement ms, Object parameter,
                             RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {

    }

    /**
     * {@link Executor#query(MappedStatement, Object, RowBounds, ResultHandler, CacheKey, BoundSql)} 操作后置处理
     * <p>
     *
     * @param executor      Executor(可能是代理对象)
     * @param ms            MappedStatement
     * @param parameter     parameter
     * @param rowBounds     rowBounds
     * @param resultHandler resultHandler
     * @param boundSql      boundSql
     * @param processResult Invocation#proceed()执行完成后的结果
     */
    default Object afterQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
                              ResultHandler resultHandler, BoundSql boundSql, Object processResult) throws SQLException {
        return processResult;
    }

    /**
     * 判断是否执行 {@link Executor#update(MappedStatement, Object)}
     * <p>
     * 如果不执行update操作,则影响行数的值为 -1
     *
     * @param executor  Executor(可能是代理对象)
     * @param ms        MappedStatement
     * @param parameter parameter
     */
    default boolean willDoUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {
        return true;
    }

    /**
     * {@link Executor#update(MappedStatement, Object)} 操作前置处理
     * <p>
     *
     * @param executor  Executor(可能是代理对象)
     * @param ms        MappedStatement
     * @param parameter parameter
     */
    default void beforeUpdate(Executor executor, MappedStatement ms, Object parameter) throws SQLException {

    }

    /**
     * {@link Executor#update(MappedStatement, Object)} 操作前置处理
     * <p>
     *
     * @param executor      Executor(可能是代理对象)
     * @param ms            MappedStatement
     * @param parameter     parameter
     * @param processResult Invocation#proceed()执行完成后的结果
     */
    default Object afterUpdate(Executor executor, MappedStatement ms, Object parameter, Object processResult) throws SQLException {
        return processResult;
    }

    /**
     * {@link ResultSetHandler#handleResultSets(Statement)} 操作前置处理
     * <p>
     *
     * @param rsh       ResultSetHandler(可能是代理对象)
     * @param statement Statement
     */
    default void beforeHandleResultSets(ResultSetHandler rsh, Statement statement) throws SQLException {

    }

    /**
     * {@link ResultSetHandler#handleResultSets(Statement)} 操作后置处理
     * <p>
     *
     * @param rsh           ResultSetHandler(可能是代理对象)
     * @param statement     Statement
     * @param processResult Invocation#proceed()执行完成后的结果
     */
    default Object afterHandleResultSets(ResultSetHandler rsh, Statement statement, Object processResult) throws SQLException {
        return processResult;
    }

    /**
     * 是否强制处理结果
     *
     * @param rsh       ResultSetHandler(可能是代理对象)
     * @param statement Statement
     * @return match or not
     * @throws SQLException e
     */
    default boolean enableForceHandleResultSets(ResultSetHandler rsh, Statement statement) throws SQLException {
        return false;
    }

    /**
     * 强制处理结果
     *
     * @param rsh       ResultSetHandler(可能是代理对象)
     * @param statement Statement
     * @return result
     * @throws SQLException e
     */
    default Object forceHandleResultSets(ResultSetHandler rsh, Statement statement) throws SQLException {
        throw new UnsupportedOperationException("forceHandleResultSets");
    }

    /**
     * {@link StatementHandler#prepare(Connection, Integer)} 操作前置处理
     * <p>
     *
     * @param sh                 StatementHandler(可能是代理对象)
     * @param connection         Connection
     * @param transactionTimeout transactionTimeout
     */
    default void beforePrepare(StatementHandler sh, Connection connection, Integer transactionTimeout) {

    }

    /**
     * {@link StatementHandler#getBoundSql()} 操作前置处理
     * <p>
     * 只有 {@link BatchExecutor} 和 {@link ReuseExecutor} 才会调用到这个方法
     *
     * @param sh StatementHandler(可能是代理对象)
     */
    default void beforeGetBoundSql(StatementHandler sh) {

    }

    /**
     * {@link StatementHandler#parameterize(Statement)} 操作前置处理
     * <p>
     * 只有 {@link BatchExecutor} 和 {@link ReuseExecutor} 才会调用到这个方法
     *
     * @param sh        StatementHandler(可能是代理对象)
     * @param statement Statement
     */
    default void beforeParameterize(StatementHandler sh, Statement statement) {

    }

    /**
     * 是否强制处理参数
     *
     * @param sh        StatementHandler(可能是代理对象)
     * @param statement Statement
     * @return match or not
     * @throws SQLException e
     */
    default boolean enableForceParameterize(StatementHandler sh, Statement statement) throws SQLException {
        return false;
    }

    /**
     * 强制处理参数
     *
     * @param sh        StatementHandler(可能是代理对象)
     * @param statement Statement
     * @throws SQLException e
     */
    default void forceParameterize(StatementHandler sh, Statement statement) throws SQLException {

    }

    /**
     * 设置属性值
     *
     * @param properties properties
     */
    default void setProperties(Properties properties) {

    }

    /**
     * 顺序号
     *
     * @return order value
     */
    default int order() {
        return Integer.MAX_VALUE;
    }
}
