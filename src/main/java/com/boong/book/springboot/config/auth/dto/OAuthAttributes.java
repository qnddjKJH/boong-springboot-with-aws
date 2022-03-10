package com.boong.book.springboot.config.auth.dto;

import com.boong.book.springboot.domain.user.Role;
import com.boong.book.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // OAuth2User 에서 반환하는 사용자 정보는 Map 이기 때문에 값 하나하나를 변환해야만 한다.
    public static OAuthAttributes of(String registrationId, String userNameAttributeKey, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }
        return ofGoogle(userNameAttributeKey, attributes);
    }

    // 구글 서비스
    private static OAuthAttributes ofGoogle(String userNameAttributeKey, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeKey)
                .build();
    }

    // 네이버 서비스
    private static OAuthAttributes ofNaver(String userNameAttributeKey, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        System.out.println(response.toString());
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeKey)
                .build();
    }

    // User Entity 생성
    // OAuthAttributes 에서 Entity 생성하는 시점은 가입 시점
    // 가입할 때의 기본 권한을 GUEST 로 주기 위해서 role 빌더값에 Role.GUEST 사용OAuthAttributes
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }

     /** User 클래스 그대로 사용하면 안되는 이유
     * Failed to convert from type [java.lang.Object] to type [byte[]] for value '해당 프로젝트@User'
     *
      * 직렬화를 구현하지 않았기 때문 그렇다면 User 클래스에 직렬화 코드를 넣으면 되지 않느냐?
      * -> 하면 안된다. User 클래스는 Entity 클래스 이기 때문이다. Entity 클래스에는 언제 어디서 다른
      *     Entity 와 관계가 형성될지 아무도 모른다. 자식 Entity 를 가지고 있다면 직렬화 대상에 자식들까지
      *     포함되어서 '성능 이슈, 부수 효과'가 발생할 확률이 높다.
      * -> 그래서 직렬화 기능을 가진 Session Dto 를 하나 추가로 만드는 것이 추후 유지 보수 및 운영에 많은 도움이 된다
     * */
}
