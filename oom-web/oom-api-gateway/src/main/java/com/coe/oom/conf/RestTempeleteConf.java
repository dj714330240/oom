package com.coe.oom.conf;

/**
 * @program: oom
 * @description: 网关服务配置
 * @author: 邓太阳
 * @create: 2019-05-10 16:59
 **/
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTempeleteConf {

    @Value("30000000")
    private Integer readTimeout;

    @Value("100000000")
    private Integer connectTimeout;

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(readTimeout);// ms
        factory.setConnectTimeout(connectTimeout);// ms
        return factory;
    }
}
