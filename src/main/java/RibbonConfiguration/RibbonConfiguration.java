package RibbonConfiguration;

import com.netflix.loadbalancer.IRule;
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
        return new RandomRule();
    }
}
