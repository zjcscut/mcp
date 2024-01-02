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
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author throwable
 * @version v1.0
 * @description
 * @since 2020/1/22 9:56
 */
@Data
@ConfigurationProperties(prefix = MybatisProperties.PREFIX)
public class MybatisProperties {

    public static final String PREFIX = "spring.mybatis";

    private Map<String, String> properties;

    private List<String> mapperLocations;

    private String mapperPackages;

    private String configLocation;

    private String typeAliasesPackage;

    private String typeHandlersPackage;

    private List<MybatisPlugin> plugins;

    private static final ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();

    public Resource[] getMapperResourceArray() {
        if (null == mapperLocations) {
            return new Resource[0];
        }
        List<Resource> resources = Lists.newArrayList();
        for (String location : mapperLocations) {
            try {
                resources.addAll(Arrays.asList(RESOLVER.getResources(location)));
            } catch (IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return resources.toArray(new Resource[0]);
    }
}
