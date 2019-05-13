package com.coe.oom.base;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@FeignClient(value="oom-api-gateway", path="oom-base-core/user")
public interface UserService {

    @RequestMapping("get")
    public Map<String,Object> getUser(Long id);
}
