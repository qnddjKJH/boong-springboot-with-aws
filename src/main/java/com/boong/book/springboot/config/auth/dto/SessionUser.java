package com.boong.book.springboot.config.auth.dto;

import com.boong.book.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    // SessionUser 에는 '인증된 사용자 정보' 만 필요
    // 인증되어서 넘어온 사용자 정보 이기에 다른 정보는 필요없다.

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
