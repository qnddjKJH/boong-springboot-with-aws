package com.boong.book.springboot.config;

import com.boong.book.springboot.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    // LoginUserArgumentResolver 가 스프링에서 인식될 수 있도록 WebMvcConfigurer 에 추가하는 설정 파일
    // HandlerMethodArgumentResolver 는 항상 WebMvcConfigurer 의 addArgumentResolvers() 를 통해서 추가되어야 한다.
    // 다른 HandlerMethodArgumentResolver 가 필요하다면 같은 방식으로 추가해 주면 된다.

    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserArgumentResolver);
    }
}
