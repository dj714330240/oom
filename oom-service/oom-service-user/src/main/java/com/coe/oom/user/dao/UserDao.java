package com.coe.oom.user.dao;

import com.coe.oom.ent.user.UserEnt;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    UserEnt getUserByCode(String userCode);
}
