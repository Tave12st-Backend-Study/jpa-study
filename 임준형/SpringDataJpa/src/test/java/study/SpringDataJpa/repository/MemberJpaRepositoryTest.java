package study.SpringDataJpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
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
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    void basicCrud() {
        Member member1 = Member.builder()
                .username("teamA")
                .age(13)
                .build();

        Member member2 = Member.builder()
                .username("teamA")
                .age(13)
                .build();

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        // 리스트 조회 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member member1 = Member.builder()
                .username("teamA")
                .age(13)
                .build();

        Member member2 = Member.builder()
                .username("teamA")
                .age(23)
                .build();
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        int i = 20;
        List<Member> teamA = memberJpaRepository.findByUsernameAndAgeGreaterThen("teamA", i);
        Member findMember = teamA.get(0);
        findMember.equals(member2);
    }

    @Test
    void paging() {
        for (int i = 1; i < 10; i++) {
            Member member = Member.builder()
                    .username("user" + i)
                    .age(10)
                    .build();
            memberJpaRepository.save(member);
        }

        int age = 10;
        int offset = 0;
        int limit = 3;

        List<Member> byPage = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age, offset, limit);

        byPage.forEach(System.out::println);
        System.out.println("totalCount = " + totalCount);
    }

    @Test
    void bulkUpdate() {
        for (int i = 1; i < 30; i++) {
            Member member = Member.builder()
                    .username("user" + i)
                    .age(10 + i)
                    .build();
            memberJpaRepository.save(member);
        }

        int resultCount = memberJpaRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(20);
    }
}