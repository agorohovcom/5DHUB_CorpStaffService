package com.agorohov.srp_stf.company_service.feign;

import com.agorohov.srp_stf.company_service.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceFeignClient {

    @GetMapping("/users/by-ids")
    List<UserDto> getUsersByIds(@RequestParam("ids") List<Long> ids);

    @GetMapping("/users/exists/{id}")
    boolean existsById(@PathVariable("id") long id);

    // TODO удалить?
//    @GetMapping("users/{id}")
//    UserDto getUserByIds(@PathVariable("id") long id);
}
