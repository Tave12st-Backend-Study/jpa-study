package querydsl.querydsl.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(of = {"id", "username", "age"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;

    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public void mappingTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }

    @Builder
    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if (team != null) {
            mappingTeam(team);
        }
    }

    /**
     *     이번에 안 사실인데, @Builder를 사용한다면 필드를 선택적으로 값을 받는 생성자를
     *     굳이 만들 필요가 없다. 자동생성된다.. 와우..
     *
     *     public Member(String username) {
     *         this(username, 0, null);
     *     }
     *
     *     public Member(String username, int age) {
     *         this(username, age, null);
     *     }
     */
}
