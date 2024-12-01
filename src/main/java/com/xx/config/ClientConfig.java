package com.xx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ClientConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 添加请求拦截器（这里只是示例，可根据实际需求添加具体逻辑，比如添加认证头信息等）
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add((request, body, execution) -> {
            request.getHeaders().add("User-Agent", "MyApp");
            return execution.execute(request, body);
        });
        restTemplate.setInterceptors(interceptors);

        // 配置消息转换器（比如确保可以正确处理JSON数据）
        List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
        converters.add(new MappingJackson2HttpMessageConverter());
        //converters.add(new StringConverter(StandardCharsets.UTF_8));
        restTemplate.setMessageConverters(converters);

        return restTemplate;
    }
}
