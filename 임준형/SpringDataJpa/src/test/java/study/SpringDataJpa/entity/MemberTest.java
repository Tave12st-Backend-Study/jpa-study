package study.SpringDataJpa.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.SpringDataJpa.repository.MemberRepository;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @Autowired
    EntityManager em;

    @Autowired MemberRepository memberRepository;

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

    @Test
    void jpaEventBaseEntity() throws InterruptedException {
        // given
        Member member = new Member("member1", 21, null);
        memberRepository.save(member);  // @PrePersist

        Thread.sleep(1000);
        member.setUsername("member2");

        em.flush();
        em.clear();

        Member findMember = memberRepository.findById(member.getId()).get();

        LocalDateTime createdDate = findMember.getCreatedDate();
        System.out.println("createdDate = " + createdDate);

        LocalDateTime updateDate = findMember.getLastModifiedDate();
        System.out.println("updateDate = " + updateDate);

        String createdBy = findMember.getCreatedBy();
        System.out.println("createdBy = " + createdBy);

        String lastModifiedBy = findMember.getLastModifiedBy();
        System.out.println("lastModifiedBy = " + lastModifiedBy);
    }
}