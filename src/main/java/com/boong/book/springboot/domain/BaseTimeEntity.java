package com.boong.book.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass   // JPA Entity 클래스들이 BaseTimeEntity 을 상속할 경우 필드들(createDate, modifiedDate)도 Column 으로 인식
@EntityListeners(AuditingEntityListener.class)  // BaseTimeEntity 클래스에 Auditing 기능을 포함시킵니다.
public class BaseTimeEntity {

    @CreatedDate    // Entity 가 생성되어 저장될 때 시간이 자동 저장
    private LocalDateTime createDate;

    @LastModifiedDate   // 조회한 Entity 의 값이 변활 때 자동으로 시간이 변경됨
    private LocalDateTime modifiedDate;
}
