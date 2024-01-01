package cn.vlts.example.repository.mapper;


import cn.vlts.example.model.entity.Foo;

/**
 * @author throwable
 * @version v1
 * @description
 * @since 2023/12/22 11:22
 */
public interface FooMapper {

    int insertSelective(Foo foo);
}
