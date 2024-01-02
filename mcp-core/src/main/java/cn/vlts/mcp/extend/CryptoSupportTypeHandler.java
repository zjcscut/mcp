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

package cn.vlts.mcp.extend;


import cn.vlts.mcp.crypto.DuplexStringCryptoProcessor;
import cn.vlts.mcp.util.McpStringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 基于加解密处理器包装出来的TypeHandler
 *
 * @author throwable
 * @version v1
 * @description 基于加解密处理器包装出来的TypeHandler
 * @since 2023/12/26 15:37
 */
public class CryptoSupportTypeHandler extends BaseTypeHandler<String> {

    private final DuplexStringCryptoProcessor cryptoProcessor;

    private CryptoSupportTypeHandler(DuplexStringCryptoProcessor cryptoProcessor) {
        this.cryptoProcessor = cryptoProcessor;
    }

    public static CryptoSupportTypeHandler newInstance(DuplexStringCryptoProcessor cryptoProcessor) {
        return new CryptoSupportTypeHandler(cryptoProcessor);
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        String p = cryptoProcessor.processBeforeEncryption(parameter);
        p = cryptoProcessor.processEncryption(p);
        p = cryptoProcessor.processAfterEncryption(p);
        ps.setString(i, p);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String columnValue = rs.getString(columnName);
        if (McpStringUtils.X.isNotEmpty(columnValue)) {
            String p = cryptoProcessor.processBeforeDecryption(columnValue);
            p = cryptoProcessor.processDecryption(p);
            p = cryptoProcessor.processAfterDecryption(p);
            return p;
        }
        return columnValue;
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String columnValue = rs.getString(columnIndex);
        if (McpStringUtils.X.isNotEmpty(columnValue)) {
            String p = cryptoProcessor.processBeforeDecryption(columnValue);
            p = cryptoProcessor.processDecryption(p);
            p = cryptoProcessor.processAfterDecryption(p);
            return p;
        }
        return columnValue;
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String columnValue = cs.getString(columnIndex);
        if (McpStringUtils.X.isNotEmpty(columnValue)) {
            String p = cryptoProcessor.processBeforeDecryption(columnValue);
            p = cryptoProcessor.processDecryption(p);
            p = cryptoProcessor.processAfterDecryption(p);
            return p;
        }
        return columnValue;
    }
}
