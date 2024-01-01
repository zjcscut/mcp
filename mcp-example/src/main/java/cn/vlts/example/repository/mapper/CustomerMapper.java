package cn.vlts.example.repository.mapper;


import cn.vlts.example.model.entity.Customer;
import org.apache.ibatis.annotations.Param;

/**
 * @author throwable
 * @version v1
 * @description
 * @since 2023/12/21 16:32
 */
public interface CustomerMapper {

    Customer selectByPrimaryKey(@Param("id") Long id);

    int insertSelective(Customer customer);

    int updateByPrimaryKeySelective(Customer customer);
}
