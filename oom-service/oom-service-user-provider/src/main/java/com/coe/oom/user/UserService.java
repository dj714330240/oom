package com.coe.oom.user;

import com.coe.oom.ent.user.UserEnt;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(value = "oom-api-gateway", path = "oom-service-user/user")
public interface UserService {

    @RequestMapping("getUserByCode")
    UserEnt getUserByUserCode(@RequestBody String userCode);
}
