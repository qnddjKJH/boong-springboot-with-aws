package com.boong.book.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter // 모든 필드의 getter 생성
@RequiredArgsConstructor // 선언된 모든 final 필드가 포함된 생성자 생성, final 필드가 없으면 포함 안됨
public class HelloResponseDto {

    private final String name;
    private final int amount;
}
