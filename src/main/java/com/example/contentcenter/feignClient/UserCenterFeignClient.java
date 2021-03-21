package com.example.contentcenter.feignClient;


import com.example.contentcenter.configuration.GlobalFeignConfiguration;
import com.example.contentcenter.feignClient.fallback.UserCenterFeignClientFallback;
import com.example.contentcenter.feignClient.fallbackfactory.UserCenterFeignClientFallbackFactory;
import com.example.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient(name = "user-center",configuration = GlobalFeignConfiguration.class)
@FeignClient(name = "user-center",
//        fallback = UserCenterFeignClientFallback.class,
        fallbackFactory = UserCenterFeignClientFallbackFactory.class
)
public interface UserCenterFeignClient {
    /**
     * http://user-center/users/{id}
     *
     * @param id
     * @return
     */
    @GetMapping("/users/{id}")
    UserDTO findById(@PathVariable Integer id);
}
