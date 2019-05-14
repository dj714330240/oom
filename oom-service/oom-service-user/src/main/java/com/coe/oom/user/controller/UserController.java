package com.coe.oom.user.controller;

import com.coe.oom.ent.user.UserEnt;
import com.coe.oom.user.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: oom
 * @description:
 * @author: 邓太阳
 * @create: 2019-05-14 16:47
 **/
@RestController
public class UserController {

    @Autowired
    private UserDao userDao;

    @RequestMapping("/user/getUserByCode")
    public UserEnt getUserByCode(@RequestBody String userCode){
        return userDao.getUserByCode(userCode);
    }

}
