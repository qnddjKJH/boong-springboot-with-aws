package com.boong.book.springboot.web.controller;

import com.boong.book.springboot.config.auth.LoginUser;
import com.boong.book.springboot.config.auth.dto.SessionUser;
import com.boong.book.springboot.service.posts.PostsService;
import com.boong.book.springboot.web.dto.PostsListResponseDto;
import com.boong.book.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class IndexController {

    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(@LoginUser SessionUser user, Model model) {
        List<PostsListResponseDto> posts = postsService.findAllDesc();
        model.addAttribute("posts", posts);

//        SessionUser user = (SessionUser) httpSession.getAttribute("user");
//         CustomOAuth2UserService 에서 로그인 성공 시 세션에 SessionUser 를 저장하도록 구성
//         즉, 로그인 성공 시 httpSession.getAttribute("user") 에서 값을 가져올 수 있다.
        /** @LoginUser 생성으로 이제 파라미터에서 세션 정보를 가져올 수 있게 되었다. */

        if (user != null) {
            // 세션에 저장된 값이 있을 때만 model 에 userName 으로 등록
            // 저장된 값이 없으면 model 에는 아무런 값이 없는 상태이므로 로그인 버튼이 보이게 된다.
            model.addAttribute("userName", user.getName());
        }
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto posts = postsService.findById(id);
        model.addAttribute("posts", posts);
        return "posts-update";
    }
}