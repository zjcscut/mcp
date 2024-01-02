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

package cn.vlts.example.config;


import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2020/3/12 11:08
 */
@RequiredArgsConstructor
@Configuration
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@AutoConfigureAfter(value = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(value = {MybatisProperties.class})
public class MybatisAutoConfiguration {

    private final MybatisProperties mybatisProperties;

    @Value("${spring.profiles.active:product}")
    public String env;

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource dataSource, BeanFactory beanFactory) {
        MybatisProperties mybatis = mybatisProperties;
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        if (null != mybatis.getProperties()) {
            Properties cp = new Properties();
            for (Map.Entry<String, String> pair : mybatis.getProperties().entrySet()) {
                cp.setProperty(pair.getKey(), pair.getValue());
            }
            bean.setConfigurationProperties(cp);
        }
        bean.setConfigLocation(new ClassPathResource(mybatisProperties.getConfigLocation()));
        bean.setMapperLocations(mybatis.getMapperResourceArray());
        if (Objects.nonNull(mybatis.getTypeAliasesPackage())) {
            bean.setTypeAliasesPackage(mybatis.getTypeAliasesPackage());
        }
        if (Objects.nonNull(mybatis.getPlugins())) {
            List<Interceptor> interceptors = Lists.newArrayList();
            for (MybatisPlugin plugin : mybatis.getPlugins()) {
                String enableEnv = plugin.getEnableEnv();
                if (Objects.isNull(enableEnv) || enableEnv.contains(env)) {
                    Interceptor interceptor = beanFactory.getBean(plugin.getBeanName(), Interceptor.class);
                    if (null != plugin.getProperties()) {
                        Properties cp = new Properties();
                        for (Map.Entry<String, String> pair : plugin.getProperties().entrySet()) {
                            cp.setProperty(pair.getKey(), pair.getValue());
                        }
                        cp.put("beanFactory", beanFactory);
                        interceptor.setProperties(cp);
                    }
                    interceptors.add(interceptor);
                }
            }
            bean.setPlugins(interceptors.toArray(new Interceptor[0]));
        }
        bean.setTypeHandlersPackage(mybatis.getTypeHandlersPackage());
        return bean;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}

