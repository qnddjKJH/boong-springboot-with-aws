package com.boong.book.springboot.web.controller;

import com.boong.book.springboot.domain.posts.Posts;
import com.boong.book.springboot.domain.posts.PostsRepository;
import com.boong.book.springboot.web.dto.PostsSaveRequestDto;
import com.boong.book.springboot.web.dto.PostsUpdateRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    /*가장 큰 차이점이라면 Servlet Container를 사용하느냐 안 하느냐의 차이입니다. MockMvc는 Servlet Container를 생성하지 않습니다.
    반면, @SpringBootTest와 TestRestTemplate은 Servlet Container를 사용합니다. 그래서 마치 실제 서버가 동작하는 것처럼
    (물론 몇몇 빈은 Mock 객체로 대체될 수는 있습니다) 테스트를 수행할 수 있습니다.
    또한, 테스트하는 관점도 서로 다릅니다. MockMvc는 서버 입장에서 구현한 API를 통해 비즈니스 로직이 문제없이 수행되는지 테스트를 할 수 있다면,
     TestRestTemplate은 클라이언트 입장에서 RestTemplate을 사용하듯이 테스트를 수행할 수 있습니다.*/

    // Mock 과 크게 다를게 없다.
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @LocalServerPort
    private int port;
    
    /** SpringBootTest 에서 MockMvc 사용하는 법 */
    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }



    @After
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="USER")
    // 임의의 사용자 인증을 추가(인증된 모의 즉 가짜 사용자 생성)
    // roles 에 권한 추가 가능
    // --> ROLE_USER 권한을 가진 사용자가 API 를 요청하는 거과 같은 효과를 가진다.
    // 이러고 실행하면 될 거 같지만 보이지 않는가? 이 어노테이션은 MockMvc 에서만 작동한다.
    public void postsCreate() throws Exception {
        // given
        String title = "title";
        String content = "content";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto
                .builder()
                .title(title)
                .content(content)
                .author("qnddjkjh.dev@gmail.com")
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        // post 방식으로 url 에 해당 url 이 필요로 하는 데이터(requestDto)와 리턴타입(Long) 을 표기
        // when
//        ResponseEntity<Long> responseEntity = testRestTemplate.postForEntity(url, requestDto, Long.class);
        mvc.perform(
                MockMvcRequestBuilders.post(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
                ).andExpect(MockMvcResultMatchers.status().isOk());

        // then
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getAuthor()).isEqualTo("qnddjkjh.dev@gmail.com");
    }

    @Test
    @WithMockUser(roles = "USER")
    public void postsUpdate() throws Exception {
        //given
        Posts savedPosts = postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        Long updateId = savedPosts.getId();
        String expectedTitle = "update title";
        String expectedContent = "update content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                .title(expectedTitle)
                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // when
//        ResponseEntity<Long> responseEntity = testRestTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);

        // MockMvc 를 통해 API 테스트를 한다.
        // 본문(Body) 영역은 문자열로 표현하기 위해 ObjectMapper 를 통해 문자열 JSON 으로 변환
        mvc.perform(
                MockMvcRequestBuilders.put(url)
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(new ObjectMapper().writeValueAsString(requestDto))
        ).andExpect(MockMvcResultMatchers.status().isOk());

        // given
//        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> allPosts = postsRepository.findAll();
        assertThat(allPosts.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(allPosts.get(0).getContent()).isEqualTo(expectedContent);
    }
}