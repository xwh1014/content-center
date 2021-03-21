package com.example.contentcenter.feignClient.fallback;


import com.example.contentcenter.feignClient.UserCenterFeignClient;
import com.example.domain.dto.user.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {
    @Override
    public UserDTO findById(Integer id) {
        UserDTO userDTO = new UserDTO();
        userDTO.setWxNickname("流控/降级返回的用户");
        return userDTO;
    }
}