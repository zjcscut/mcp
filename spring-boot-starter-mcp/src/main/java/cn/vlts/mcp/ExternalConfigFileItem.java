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
