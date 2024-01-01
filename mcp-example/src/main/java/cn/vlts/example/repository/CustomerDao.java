package cn.vlts.example.repository;


import cn.vlts.example.model.query.result.CustomerAnnoResult;
import cn.vlts.example.model.query.result.CustomerConfigResult;
import cn.vlts.example.model.query.result.CustomerResult;
import cn.vlts.example.repository.mapper.CustomerMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author throwable
 * @version v1
 * @description
 * @since 2023/12/21 16:32
 */
@Repository
public interface CustomerDao extends CustomerMapper {

    CustomerResult selectOneById(@Param("id") Long id);

    CustomerAnnoResult selectOneAnnoById(@Param("id") Long id);

    CustomerConfigResult selectOneConfigById(@Param("id") Long id);
}
