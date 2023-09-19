package hellojpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1,
        allocationSize = 1
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE
            ,generator = "MEMBER_SEQ_GENERATOR")
    private Long id;
    @Column(name = "name",nullable = false)
    private String username;
}
