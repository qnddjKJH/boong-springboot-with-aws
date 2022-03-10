package com.boong.book.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {

    /**
     * @Target
     * 이 어노테이션이 생성될 수 있는 위치를 지정합니다.
     * 현재 PARAMETER 로 지정하여서 메소드의 파라미터로 선언된 객체에서만 사용할 수 있습니다.
     *
     * @Retention
     * 라이프 사이클 해당 어노테이션이 언제까지 살아 남아 있을지를 지정해준다.
     * 런타임까지 지정되어있다. = 사실상 안 사라집니다.
     */

}
