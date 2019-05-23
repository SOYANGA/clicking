package com.github.soyanga.secondskillsystem.dao;

import com.github.soyanga.secondskillsystem.domain.MiaoShaUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @program: seconds_kill_system _2
 * @Description: 秒杀实体类的DAO层
 * @Author: SOYANGA
 * @Create: 2019-05-16 14:58
 * @Version 1.0
 */
@Mapper
@Repository
public interface MiaoshaUserDao {

    /**
     * 根据id（用户手机号）
     * @param id 手机号
     * @return 返回秒杀用户对象
     */
    @Select("select * from miaosha_user where id = #{id}")
    public MiaoShaUser getById(@Param("id") long id);
}
