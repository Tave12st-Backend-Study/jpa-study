package study.SpringDataJpa.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 새로운 Entity인지 검증하기 위한 클래스
 */

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    public Item(String id) {
        this.id = id;
    }

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        // 생성이 된 적이 있는지 시간으로 확인
        return createdDate == null;
    }

    /**
     * em.persis() 당시에 id 값이 채워지므로 처음 저장 당시에는 id가 null이다.
     * 하지만 GenerationType을 못쓰게 된다면, 식별자가 존재하므로 새로운 엔티티로 인식하지 않는다.
     * 그렇기 때문에 비효율적으로 em.merge를 하기 위해 select하고 다시 insert를 한다.
     * 이를 막기 위해 제공하는 인터페이스가 있다.
     */
}
