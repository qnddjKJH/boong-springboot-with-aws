package com.boong.book.springboot.web.controller;

import com.boong.book.springboot.config.auth.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloController.class,
        excludeFilters = {
            @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = SecurityConfig.class)
        }
)
// WebMvcTest 는 CustomOAuth2UserService 를 스캔하지 않는다.
// WebMvcTest 스캔 대상
// WebSecurityConfigureAdapter, WebMvcConfigurer, @ControllerAdvice, @Controller
// 비 스캔 대상
// @Service, @Repository, @Component
// 그러니 SecurityConfig 는 읽었지만 SecurityConfig 를 생성하기 위해 필요한
// CustomOAuth2UserService (@Service) 를 읽을 수가 없어 에러가 발생
// 해결법 -> SecurityConfig 클래스 스캔대상에서 제외
// @WebMvcTest 의 secure 옵션은 2.1 부터 deprecated 되었다. 언제 삭제될지 모르니 사용하지 말것
public class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(roles = "USER")
    public void hello() throws Exception {
        String hello = "hello";

        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    @WithMockUser(roles = "USER")
    // IllegalArgumentException: At least one JPA metamodel must be present!
    // 해당 에러는 @EnableJpaAuditing 으로 인해 발생한다.
    // 사용하기 위해서 최소 한개 이상의 @Entity 클래스가 필요하다.
    // @WebMvcTest 이다 보니 당연히 없다
    // 지금 시점에 @EnableJpaAuditing 은 Application 에 @SpringBootApplication 과 함께 있음
    // 해결법 -> @EnableJpaAuditing 을 따로 떼내어서 @SpringBootApplication 스캔도중
    // 같이 스캔되지 않게 분리한다. -> config/JpaConfig 에 분리
    public void helloDtoTest() throws Exception {
        String name = "name";
        int amount = 10000;

        mockMvc.perform(get("/hello/dto")
                        .param("name", name)
                        .param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name)))
                .andExpect(jsonPath("$.amount", is(amount)));
    }

}