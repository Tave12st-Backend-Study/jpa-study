package study.SpringDataJpa.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void testEntity() {
        Team teamA = Team.builder()
                .name("teamA")
                .build();
        Team teamB = Team.builder()
                .name("teamB")
                .build();

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = Member.builder()
                .username("userA")
                .age(10)
                .team(teamA)
                .build();

        Member member2 = Member.builder()
                .username("userA")
                .age(20)
                .team(teamA)
                .build();

        Member member3 = Member.builder()
                .username("userA")
                .age(30)
                .team(teamB)
                .build();

        Member member4 = Member.builder()
                .username("userA")
                .age(40)
                .team(teamB)
                .build();

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        // 강제 초기화
        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m FROM Member m", Member.class)
                .getResultList();

        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }
}