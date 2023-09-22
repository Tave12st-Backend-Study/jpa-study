package hellojpa;

import javax.persistence.*;

@Entity
@SequenceGenerator(
        name = "member_seq_generator", sequenceName = "member_seq"//매핑할 데이터베이스 시퀀스 이름
)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq_generator")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(100) default 'EMPTY'")
    private String username;

    public Member (){

    }

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
}
