package hellojpa.jpql;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Team {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private int age;
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();
}
