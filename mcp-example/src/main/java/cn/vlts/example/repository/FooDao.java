package cn.vlts.example.repository;


import cn.vlts.example.model.entity.Foo;
import cn.vlts.example.repository.mapper.FooMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author throwable
 * @version v1
 * @description
 * @since 2023/12/22 11:23
 */
@Repository
public interface FooDao extends FooMapper {

    Foo selectOneById(@Param("id") Long id);
}
