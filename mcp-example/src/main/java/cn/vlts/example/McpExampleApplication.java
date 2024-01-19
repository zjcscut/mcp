/*
 *
 *  * MIT License
 *  *
 *  * Copyright (c) 2024 vlts.cn
 *  *
 *  * Permission is hereby granted, free of charge, to any person obtaining a copy
 *  * of this software and associated documentation files (the "Software"), to deal
 *  * in the Software without restriction, including without limitation the rights
 *  * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  * copies of the Software, and to permit persons to whom the Software is
 *  * furnished to do so, subject to the following conditions:
 *  *
 *  * The above copyright notice and this permission notice shall be included in all
 *  * copies or substantial portions of the Software.
 *  *
 *  * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  * SOFTWARE.
 *
 */

package cn.vlts.example;

import cn.vlts.example.model.entity.Customer;
import cn.vlts.example.model.entity.Foo;
import cn.vlts.example.model.query.result.CustomerAnnoResult;
import cn.vlts.example.model.query.result.CustomerConfigResult;
import cn.vlts.example.model.query.result.CustomerResult;
import cn.vlts.example.repository.CustomerDao;
import cn.vlts.example.repository.FooDao;
import cn.vlts.mcp.config.McpConfig;
import cn.vlts.mcp.spi.McpCoreInterceptor;
import cn.vlts.mcp.spi.McpInternalInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Date;
import java.util.Objects;

/**
 * mcp example application
 *
 * @author throwable
 * @version v1
 * @description mcp example application
 * @since 2024/1/19 22:16
 */
@Slf4j
public class McpExampleApplication {

    public static void main(String[] args) throws Exception {
        InputStream inputStream = Resources.getResourceAsStream("mybatis.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        inputStream.close();
        init(sqlSessionFactory);
        McpConfig mcpConfig = new McpConfig();
        mcpConfig.setGlobalKey("1234567890123456");
        mcpConfig.setGlobalCryptoAlgorithm("AES_ECB_PKCS5PADDING_BASE64");
        McpInternalInterceptor mcpInternalInterceptor = new McpInternalInterceptor(mcpConfig);
        McpCoreInterceptor mcpInterceptor = new McpCoreInterceptor();
        mcpInterceptor.addInnerInterceptor(mcpInternalInterceptor);
        sqlSessionFactory.getConfiguration().addInterceptor(mcpInterceptor);
        try (SqlSession session = sqlSessionFactory.openSession()) {
            FooDao fooDao = session.getMapper(FooDao.class);
            CustomerDao customerDao = session.getMapper(CustomerDao.class);
            Foo foo = new Foo();
            foo.setName("bar");
            fooDao.insertSelective(foo);
            Foo bar = fooDao.selectOneById(1L);
            if (Objects.isNull(bar)) {
                throw new RuntimeException("foo");
            }
            Customer customer = new Customer();
            customer.setCustomerName("doge-1");
            customer.setPhone("12345678901");
            customer.setCreateTime(new Date());
            int count = customerDao.insertSelective(customer);
            log.info("插入数据成功,更新记录数:{}", count);
            Customer c = customerDao.selectByPrimaryKey(1L);
            CustomerResult customerResult = customerDao.selectOneById(1L);
            CustomerAnnoResult annoResult = customerDao.selectOneAnnoById(1L);
            CustomerConfigResult configResult = customerDao.selectOneConfigById(1L);
            log.info("customerResult => {}", customerResult);
            log.info("annoResult => {}", annoResult);
            log.info("configResult => {}", configResult);
            Customer updater = new Customer();
            updater.setId(1L);
            updater.setCustomerName("doge-2");
            count = customerDao.updateByPrimaryKeySelective(updater);
            log.info("更新数据成功,更新记录数:{}", count);
            customerResult = customerDao.selectOneById(1L);
            annoResult = customerDao.selectOneAnnoById(1L);
            configResult = customerDao.selectOneConfigById(1L);
            log.info("更新后customerResult => {}", customerResult);
            log.info("更新后annoResult => {}", annoResult);
            log.info("更新后configResult => {}", configResult);
        }
    }

    private static void init(SqlSessionFactory sqlSessionFactory) throws Exception {
        File file = Resources.getResourceAsFile("schema.sql");
        byte[] bytes = Files.readAllBytes(file.toPath());
        String ddl = new String(bytes, StandardCharsets.UTF_8);
        try (SqlSession session = sqlSessionFactory.openSession();
             Connection connection = session.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(ddl);
        }
    }
}
