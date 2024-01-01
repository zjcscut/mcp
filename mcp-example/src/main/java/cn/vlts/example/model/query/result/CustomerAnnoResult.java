package cn.vlts.example.model.query.result;


import cn.vlts.mcp.spi.CryptoField;
import lombok.Data;

import java.util.Date;

/**
 * @author throwable
 * @version v1
 * @description 通过类成员注解定义加解密规则的结果
 * @since 2023/12/21 16:30
 */
@Data
public class CustomerAnnoResult {

    private Long id;

    @CryptoField
    private String phone;

    @CryptoField
    private String customerName;

    private Date createTime;
}
