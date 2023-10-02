package hellojpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    private String name;

    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "city",
                    column=@Column(name = "WORK_CITY")),
                    @AttributeOverride(name = "street",
                            column=@Column(name = "WORK_STREET")),
                    @AttributeOverride(name = "zipcode",
                            column=@Column(name = "WORK_ZIPCODE"))
            }
    )
    private Address workAddress;
    @Embedded
    private Address homeAddress;
    @Embedded
    private Period workPeriod;
}
