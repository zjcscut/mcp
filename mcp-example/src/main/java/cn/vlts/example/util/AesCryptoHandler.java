package cn.vlts.example.util;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static cn.vlts.example.util.AesUtils.AES_KEY;


/**
 * @author throwable
 * @version v1
 * @description
 * @since 2023/12/22 11:34
 */
public class AesCryptoHandler extends BaseTypeHandler<String> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String o, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, AesUtils.encrypt(o, AES_KEY));
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String columnValue = resultSet.getString(s);
        return AesUtils.decrypt(columnValue, AES_KEY);
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String columnValue = resultSet.getString(i);
        return AesUtils.decrypt(columnValue, AES_KEY);
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        return AesUtils.decrypt(columnValue, AES_KEY);
    }
}
