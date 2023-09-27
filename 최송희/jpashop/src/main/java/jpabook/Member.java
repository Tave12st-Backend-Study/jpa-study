package jpabook;

import javax.persistence.*;
import java.util.List;

@Entity
public class Member {
    @Id
    @GeneratedValue //생략하면 AUTO
    @Column(name = "MEMBER_ID")
    private Long id;
    private String username;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false) //읽기전용
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

}
