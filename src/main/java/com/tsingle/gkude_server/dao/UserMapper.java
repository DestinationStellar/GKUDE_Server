package com.tsingle.gkude_server.dao;

import com.tsingle.gkude_server.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface UserMapper {
    @Select("select * from user where id=#{id}")
    User selectById(@Param("id") Long id);

    @Select("select * from user")
    List<User> selectAll();

    int insert(@Param("user") User user);

    int deleteById(@Param("id") Long id);

    int deleteAll();

    int update(@Param("user") User user);
}
