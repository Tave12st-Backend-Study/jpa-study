package reviewjpa.superclass;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity {

    @Column(name = "INSERT_MEMBER")
    private String createBy;
    private LocalDateTime createDate;
    private String lastModifedBy;
    @Column(name = "UPDATE_MEMBER")
    private LocalDateTime modifiedDate;
}
