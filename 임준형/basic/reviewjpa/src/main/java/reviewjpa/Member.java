package reviewjpa;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 1)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "name")
    private String username;

}


