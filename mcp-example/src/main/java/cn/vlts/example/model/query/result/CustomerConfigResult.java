package cn.vlts.example.model.query.result;

import lombok.Data;

import java.util.Date;

/**
 * @author throwable
 * @version v1
 * @description 通过外置配置文件定义加解密规则的结果
 * @since 2023/12/21 16:30
 */
@Data
public class CustomerConfigResult {

    private Long id;

    private String phone;

    private String customerName;

    private Date createTime;
}
