package com.github.soyanga.secondskillsystem.dao;

import com.github.soyanga.secondskillsystem.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @program: seconds_kill_system
 * @Description: DAO层User
 * @Author: SOYANGA
 * @Create: 2019-05-14 21:30
 * @Version 1.0
 */
@Mapper
@Repository
public interface UserDao {

    @Insert("insert into user(id,name) values(#{id},#{name})")
    public int insert(User user);

    @Select("select * from user where id = #{id}")
    public User getById(@Param("id") int id);
}
