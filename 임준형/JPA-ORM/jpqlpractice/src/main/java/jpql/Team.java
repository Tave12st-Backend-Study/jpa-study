package jpql;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter @Setter
public class Team {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @BatchSize(size = 50)
    @OneToMany(mappedBy = "team")
    private List<Member> memberList = new ArrayList<>();
}
