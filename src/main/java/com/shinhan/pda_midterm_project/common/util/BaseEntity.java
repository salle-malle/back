package com.shinhan.pda_midterm_project.common.util;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
// 공통 필드를 여러 엔티티에서 재사용할 수 있도록 하기 위해 사용
//  직접 테이블로 매핑되지 않지만, 이를 상속받은 하위 클래스가 해당 필드를 자신의 테이블 컬럼으로 사용
@EntityListeners(AuditingEntityListener.class)
// 엔티티의 라이프사이클(생성, 업데이트, 삭제 등) 이벤트를 감지하여 특정 로직을 수행할 수 있게 함
// 엔티티의 생성 및 수정 시점에 자동으로 값을 할당하는 역할을 수행
@Getter
public abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
