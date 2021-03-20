package com.example.contentcenter.configuration;

import RibbonConfiguration.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Configuration;

/**
 * @program content-center
 * @description:
 * @author: xiewenhui
 * @create: 2021/03/16 23:24
 */
//注释，单个配置,细粒度配置
//@Configuration
//@RibbonClient(name = "user-center",configuration = RibbonConfiguration.class)
//全局配置
@Configuration
@RibbonClients(defaultConfiguration  = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {

}
