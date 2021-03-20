package RibbonConfiguration;

import com.example.contentcenter.configuration.NacosSameClusterWeightedRule;
import com.example.contentcenter.configuration.NacosWeightedRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program content-center
 * @description:
 * @author: xiewenhui
 * @create: 2021/03/16 23:25
 */
@Configuration
public class RibbonConfiguration {
    @Bean
    public IRule RibbonRule(){
        //ribbon实现权重调用实现类
//        return new NacosWeightedRule();
          return new NacosSameClusterWeightedRule();
    }
}
