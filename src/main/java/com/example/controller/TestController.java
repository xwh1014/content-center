package com.example.controller;

import com.example.contentcenter.dao.content.ShareMapper;
import com.example.contentcenter.domain.entity.content.Share;
import com.example.contentcenter.feignClient.TestBaiduFeignClient;
import com.example.contentcenter.feignClient.TestUserCenterFeignClient;
import com.example.domain.dto.user.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @program user-center
 * @description:
 * @author: xiewenhui
 * @create: 2021/03/10 20:15
 */
@RestController
public class TestController {
    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/test")
    public List<Share>  testInsert(){
        Share share = new Share();
        //1.插入
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        share.setTitle("测试啊");
        share.setCover("xxx");
        share.setAuthor("谢文辉");
        this.shareMapper.insertSelective(share);
        //2.查询
        List<Share> shares = this.shareMapper.selectAll();

        return shares;
    }


    /**
     * 测试服务发现，证明内容中心总能找到用户中心
     * @return
     */
    @GetMapping("/test2")
    public List<ServiceInstance> setDiscoveryClient(){
        return this.discoveryClient.getInstances("user-center");
    }

    @Autowired
    private TestUserCenterFeignClient testUserCenterFeignClient;

    @GetMapping("test-get")
    public UserDTO query(UserDTO userDTO) {
        return testUserCenterFeignClient.query(userDTO);
    }

    @Autowired
    private TestBaiduFeignClient testBaiduFeignClient;

    @GetMapping("baidu")
    public String baiduIndex() {
        return this.testBaiduFeignClient.index();
    }

}
