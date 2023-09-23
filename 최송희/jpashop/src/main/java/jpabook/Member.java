package jpabook;

import javax.persistence.*;

@Entity
public class Member {
    @Id @GeneratedValue //생략하면 AUTO
    @Column(name = "MEMBER_ID")
    private Long id;
    @Column(name = "USERNAME")
    private String username;

    @ManyToOne //멤버 입장에서는 Many, 팀 입장에서는 one이므로
    @JoinColumn(name = "TEAM_ID") //조인하는 컬럼명
    private Team team;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {

        this.team = team;
        team.getMembers().add(this);
    }
}
