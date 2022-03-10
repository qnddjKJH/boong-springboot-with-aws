package com.boong.book.springboot.config.auth;

import com.boong.book.springboot.config.auth.dto.OAuthAttributes;
import com.boong.book.springboot.config.auth.dto.SessionUser;
import com.boong.book.springboot.domain.user.User;
import com.boong.book.springboot.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();    
        // 현재 로그인 진행 중인 서비스를 구분하는 코드
        // 현재는 구글만 사용하는 불필요한 값, 하지만 추후에 다른 소셜 서비스를 사용하면 서비스 구분 위해 사용합니다.
        // 추후에 네이버 로그인 연동 시 네이버 로그인인지 구글 로그인인지 구분하기 위해 사용
        String userNameAttributeName = userRequest.getClientRegistration()
                                            .getProviderDetails().getUserInfoEndpoint()
                                            .getUserNameAttributeName();
        // OAuth2 로그인 진행 시 키가 되는 필드값을 이야기 함, Primary Key 와 같은 의미
        // 구글의 경우 기본적인 코드를 지원하지만, 네이버, 카카오 등은 기본 지원하지 않는다. 구글의 기본 코드는 "sub"이다
        // 이후 네이버 로그인과 구글 로그인을 동시 지원할 때 사용

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        // OAuth2UserService 를 통해 가져온 OAuth2User 의 attribute 를 담을 클래스이다
        // 이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용
        
        User user = saveOrUpdate(attributes);

        httpSession.setAttribute("user", new SessionUser(user));
        // SessionUser 세션에 사용자 정보를 담기 위한 Dto 클래스

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );

    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        
        return userRepository.save(user);
    }
}
