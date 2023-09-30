package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    private String createBy;
    private LocalDateTime createDate;
    private String lastModifedBy;
    private LocalDateTime modifiedDate;
}
