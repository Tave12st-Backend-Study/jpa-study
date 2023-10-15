package reviewjpa;

import lombok.Getter;
import lombok.Setter;
import reviewjpa.superclass.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "TEAM_ID")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team")
    // 여기서 team은 Member객체에서 Team을 참조하는 변수 명
    private List<Member> members = new ArrayList<>();

}
