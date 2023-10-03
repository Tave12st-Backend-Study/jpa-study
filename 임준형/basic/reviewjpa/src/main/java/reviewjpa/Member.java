package reviewjpa;

import lombok.*;
import reviewjpa.superclass.BaseEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    // 기간 Period
    @Embedded
    private Period workPeriod;

    // 주소
    @Embedded
    private Address homeAddress;

    // 주소
    // 같은 클래스를 사용하면 오류가 남 이럴 때
    @Embedded
    @AttributeOverrides(value = {@AttributeOverride(name = "city",
                                    column = @Column(name = "WORK_CITY")),
                                @AttributeOverride(name = "street",
                                    column = @Column(name = "WORK_STREET")),
                                @AttributeOverride(name = "zipcode",
                                    column = @Column(name = "WORK_ZIPCODE")
                                )})
    private Address workAddress;

}
