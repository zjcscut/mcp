package cn.vlts.example.model.query.result;

import lombok.Data;

import java.util.Date;

/**
 * @author throwable
 * @version v1
 * @description 不进行加解密的结果
 * @since 2023/12/21 16:30
 */
@Data
public class CustomerResult {

    private Long id;

    private String phone;

    private String customerName;

    private Date createTime;
}
