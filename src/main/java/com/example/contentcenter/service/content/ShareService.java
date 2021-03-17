package com.example.contentcenter.service.content;

import com.example.contentcenter.dao.content.ShareMapper;
import com.example.contentcenter.domain.entity.content.Share;
import com.example.domain.dto.content.ShareDTO;
import com.example.domain.dto.user.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @program content-center
 * @description:
 * @author: xiewenhui
 * @create: 2021/03/10 22:31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareService {

    private final ShareMapper shareMapper;

    //获取实例
//    private final DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public ShareDTO findById(Integer id){
        Share share = this.shareMapper.selectByPrimaryKey(id);
        Integer userId = share.getUserId();
//        RestTemplate restTemplate = new RestTemplate();
//        List<ServiceInstance> instances = discoveryClient.getInstances("user-center");
        //获取单个实例，并抛出异常
//        String targetUrl = instances.stream()
//                .map(instance-> instance.getUri().toString() + "/users/{id}")
//                .findFirst()
//                .orElseThrow(()-> new ArrayIndexOutOfBoundsException("当前没有实例"));
//                 获取多个实例，ribbon随机
//                 List<String> targetUrls = instances.stream()
//                .map(instance-> instance.getUri().toString() + "/users/{id}")
//                .collect(Collectors.toList());
//                 int i = ThreadLocalRandom.current().nextInt(targetUrls.size());
//        log.info(targetUrls.get(i));
        UserDTO userDTO = this.restTemplate.getForObject(
                "http://user-center/users/{userId}",
                UserDTO.class,
                userId
        );
        //消息的装配
//        Share.builder().id(share.getId()).build();
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share,shareDTO);
        shareDTO.setWxNickName(userDTO.getWxNickname());
        return  shareDTO;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String forObject = restTemplate.getForObject(
                "http://localhost:9090/users/1", String.class
        );
        System.out.println(forObject);
    }
}
