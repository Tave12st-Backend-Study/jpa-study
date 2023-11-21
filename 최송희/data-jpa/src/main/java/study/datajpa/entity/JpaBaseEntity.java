package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class JpaBaseEntity {
    @Column(updatable = false) //db에 값이 변경되지 않는다.
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //persist하기 전 이벤트 발생
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now; //등록일
        updatedDate = now; //수정일(null이 아니라 초기값을 넣어주면 쿼리날릴때 편리함)
    }

    @PreUpdate
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }

}
