package com.boc.mapper;

import java.util.Collection;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boc.entite.User;

public interface UserMapper {

    @Select("SELECT * FROM user")
    Collection<User> list();
    
    @Select("SELECT * FROM user WHERE id = ${id}")
    User getById(@Param("id")Integer id);
    
}
