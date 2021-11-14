package myproject.eumfruit.entity;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@EntityListeners(value = {AuditingEntityListener.class})    // Auditing을 적용하기위한 어노테이션
@MappedSuperclass   // 공통 매핑 정보가 필요할 때 사용하는 어노테이션으로 부모 클래스를 상속 받는 자식 클래스에 매핑 정보만 제공합니다.
@Getter
public abstract class BaseEntity extends BaseTimeEntity {
    // BaseTimeEntity를 상속받는다.
    // 등록일, 수정일, 등록자, 수정자를 모두 갖는 엔티티는 BaseEntity를 상속받으면 된다.

    @CreatedBy
    @Column(updatable = false)
    private String createBy;

    @LastModifiedBy
    private String modifiedBy;
}
