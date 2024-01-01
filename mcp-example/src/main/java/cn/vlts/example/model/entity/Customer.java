package cn.vlts.example.model.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author throwable
 * @version v1
 * @description
 * @since 2023/12/21 16:29
 */
@Data
public class Customer {

    private Long id;

    private String phone;

    private String customerName;

    private Date createTime;
}
