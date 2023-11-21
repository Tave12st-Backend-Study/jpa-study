package study.SpringDataJpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.SpringDataJpa.entity.Member;
import study.SpringDataJpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Test
    public void testMember() {

        Team team = Team.builder()
                .name("teamA")
                .build();


        String username = "memberA";
        Member member = Member.builder()
                .username(username)
                .team(team)
                .build();

        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());

        assertEquals(savedMember.getId(), findMember.getId());
        assertEquals(savedMember.getUsername(), findMember.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
    }

}