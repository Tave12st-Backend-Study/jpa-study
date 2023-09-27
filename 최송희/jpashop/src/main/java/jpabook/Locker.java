package jpabook;

import javax.persistence.*;
import java.util.List;

@Entity
public class Locker {
    @Id @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;
    private String name;

    @OneToOne(mappedBy = "locker")
    private Member member;

}
