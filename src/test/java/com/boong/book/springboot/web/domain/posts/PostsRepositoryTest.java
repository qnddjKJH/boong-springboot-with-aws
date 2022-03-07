package com.boong.book.springboot.web.domain.posts;

import com.boong.book.springboot.domain.posts.Posts;
import com.boong.book.springboot.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @After
    public void cleanUp() {
        postsRepository.deleteAll();
    }

    @Test
    public void loadPost() {
        // given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()
                    .title(title)
                    .content(content)
                    .author("qnddjkjh.dev@gmail.com")
                    .build());

        // when
        List<Posts> posts = postsRepository.findAll();

        // then
        Posts post = posts.get(0);
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
    }

    @Test
    public void BaseTimeEntity_reg() {
        // given
        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
        postsRepository.save(Posts.builder()
                .title("title")
                .content("content")
                .author("author")
                .build());

        // when
        List<Posts> postsList = postsRepository.findAll();


        // then
        Posts posts = postsList.get(0);
        System.out.println(">>>>>>>> now=" + now);
        System.out.println(">>>>>>>> createDate=" + posts.getCreateDate() + ", modifiedDate=" + posts.getModifiedDate());
        assertThat(posts.getCreateDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }

}