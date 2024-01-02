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

package cn.vlts.mcp;

import java.util.List;

/**
 * 外部配置文件条目
 *
 * @author throwable
 * @version v1
 * @description 外部配置文件条目
 * @since 2023/12/21 14:56
 */
public class ExternalConfigFileItem {

    /**
     * 实体类全类名
     */
    private String className;

    /**
     * Mybatis MappedStatement ID列表
     */
    private List<String> msIdList;

    /**
     * Mybatis ResultMap ID列表
     */
    private List<String> rsmIdList;

    /**
     * 实体类字段列表
     */
    private List<ExternalConfigFieldItem> fields;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<String> getMsIdList() {
        return msIdList;
    }

    public void setMsIdList(List<String> msIdList) {
        this.msIdList = msIdList;
    }

    public List<String> getRsmIdList() {
        return rsmIdList;
    }

    public void setRsmIdList(List<String> rsmIdList) {
        this.rsmIdList = rsmIdList;
    }

    public List<ExternalConfigFieldItem> getFields() {
        return fields;
    }

    public void setFields(List<ExternalConfigFieldItem> fields) {
        this.fields = fields;
    }
}
