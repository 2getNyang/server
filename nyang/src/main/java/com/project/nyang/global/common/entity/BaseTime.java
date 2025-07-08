package com.project.nyang.global.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 *
 * BaseTime 설명입니다 
 * @fileName        : BaseTime
 * @author          : 엄아영
 * @since           : 2025-07-07
 * 
 */

@Getter
@EntityListeners(AuditingEntityListener.class)
//이 클래스를 상속받는 엔티티들은 이 클래스의 필드를 컬럼으로 포함시켜라는 어노테이션
@MappedSuperclass
public abstract class BaseTime {

    //엔티티가 저장될때 자동으로 시간을 기록
    @CreatedDate
    @Column(name = "created_at", columnDefinition = "TIMESTAMP",
            updatable = false, nullable = false)
    private LocalDateTime createdAt;

    //수정될 때마다 기록
    @LastModifiedDate
    @Column(name = "modified_at", columnDefinition = "TIMESTAMP",
            nullable = false)
    private LocalDateTime modifiedAt;

    //삭제 처리될 때마다 기록
    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP")
    private LocalDateTime deletedAt;

    //삭제 처리 메서드
    public void markDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
