package study.SpringDataJpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.SpringDataJpa.entity.Member;
import study.SpringDataJpa.entity.Team;
import study.SpringDataJpa.repository.dto.MemberDto;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    public void testMember() {

        Team team = Team.builder()
                .name("teamA")
                .build();
        teamRepository.save(team);

        String username = "memberA";
        Member member = Member.builder()
                .age(20)
                .username(username)
                .team(team)
                .build();

        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).get();

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

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
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
        memberRepository.save(member1);
        memberRepository.save(member2);

        int i = 20;
        List<Member> teamA = memberRepository.findByUsernameAndAgeGreaterThan("teamA", i);
        Member findMember = teamA.get(0);
        findMember.equals(member2);
    }

    @Test
    void testQuery() {
        Member member1 = Member.builder()
                .username("teamA")
                .age(13)
                .build();

        Member member2 = Member.builder()
                .username("teamA")
                .age(23)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        String teamA1 = "teamA";
        int age = 23;
        List<Member> result = memberRepository.findUser(teamA1, age);
        assertThat(result.get(0)).isEqualTo(member2);
    }

    @Test
    void testQueryValue() {
        Member member1 = Member.builder()
                .username("teamA")
                .age(13)
                .build();

        Member member2 = Member.builder()
                .username("teamA")
                .age(23)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        String teamA1 = "teamA";
        int age = 23;
        List<String> usernameList = memberRepository.findUsernameList();
        usernameList.forEach(System.out::println);
    }

    @Test
    void testQueryDto() {
        Team team = Team.builder()
                .name("teamA")
                .build();
        teamRepository.save(team);

        Member member1 = Member.builder()
                .username("userA")
                .age(13)
                .team(team)
                .build();

        memberRepository.save(member1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        memberDto.forEach(System.out::println);
    }

    @Test
    void testQueryParameterCollection() {
        Team team = Team.builder()
                .name("teamA")
                .build();
        teamRepository.save(team);

        Member member1 = Member.builder()
                .username("userA")
                .age(13)
                .team(team)
                .build();

        Member member2 = Member.builder()
                .username("userB")
                .age(33)
                .team(team)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "userB", "CCC"));
        byNames.forEach(System.out::println);
    }
}