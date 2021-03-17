package com.example.contentcenter.configuration;

import RibbonConfiguration.RibbonConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.annotation.Configuration;

/**
 * @program content-center
 * @description:
 * @author: xiewenhui
 * @create: 2021/03/16 23:24
 */
@Configuration
@RibbonClient(name = "user-center",configuration = RibbonConfiguration.class)
public class UserCenterRibbonConfiguration {

}
