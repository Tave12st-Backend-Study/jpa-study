package querydsl.querydsl.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static querydsl.querydsl.QuerydslApplicationTests.generateMember;
import static querydsl.querydsl.QuerydslApplicationTests.generateTeam;

import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydsl.domain.Member;
import querydsl.querydsl.domain.Team;
import querydsl.querydsl.dto.MemberSearchCondition;
import querydsl.querydsl.dto.MemberTeamDto;

@SpringBootTest
@Transactional
class PreMemberJpaRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    PreMemberJpaRepository preMemberJpaRepository;

    public void before() {
        Team teamA = generateTeam("teamA");
        Team teamB = generateTeam("teamB");

        em.persist(teamA);
        em.persist(teamB);

        Member member1 = generateMember("member1", 10, teamA);
        Member member2 = generateMember("member2", 20, teamA);
        Member member3 = generateMember("member3", 30, teamB);
        Member member4 = generateMember("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    // ----------------------------------------- 순수 JPA Query -----------------------------------------

    @Test
    void basicTest() {
        Member member = generateMember("member1", 10, null);
        preMemberJpaRepository.save(member);

        Member findMember = preMemberJpaRepository.findById(member.getId()).get();
        assertThat(member).isEqualTo(findMember);

        List<Member> result = preMemberJpaRepository.findAll();
        assertThat(result).containsExactly(member);

        List<Member> result2 = preMemberJpaRepository.findByUsername("member1");
        assertThat(result2).containsExactly(member);
    }

    // ----------------------------------------- 순수 JPA Query 끝 -----------------------------------------


    // ----------------------------------------- 순수 JPA -> QueryDsl -----------------------------------------
    @Test
    void basicTest_QueryDsl() {
        Member member = generateMember("member1", 10, null);
        preMemberJpaRepository.save(member);

        Member findMember = preMemberJpaRepository.findById(member.getId()).get();
        assertThat(member).isEqualTo(findMember);

        List<Member> result = preMemberJpaRepository.findAll_QueryDsl();
        assertThat(result).containsExactly(member);

        List<Member> result2 = preMemberJpaRepository.findByUsername_QueryDsl("member1");
        assertThat(result2).containsExactly(member);
    }

    // ----------------------------------------- 순수 JPA -> QueryDsl 끝 -----------------------------------------


    // ----------------------------------------- 동적 쿼리와 성능 최적화 조회 - BUilder 사용 -----------------------------------------
    @Test
    void searchByBuilderTest() {
        before();
        MemberSearchCondition condition = MemberSearchCondition.builder()
                .ageGoe(33)
                .ageLoe(40)
                .teamName("teamB")
                .build();

        /**
         * 동적 쿼리는 최소값이라도 있는게 좋다. 빈 값 XX
         */

        List<MemberTeamDto> memberTeamDtos = preMemberJpaRepository.searchByBuilder(condition);

        assertThat(memberTeamDtos).extracting("username").containsExactly("member4");
    }

    // ----------------------------------------- 동적 쿼리와 성능 최적화 조회 - BUilder 사용 끝 -----------------------------------------


    // ----------------------------------------- 동적 쿼리와 성능 최적화 조회 - ⭐️Where 다중 파라미터 사용 ⭐️-----------------------------------------
    @Test
    void searchByWhereParamTest() {
        before();
        MemberSearchCondition condition = MemberSearchCondition.builder()
                .ageGoe(33)
                .ageLoe(40)
                .teamName("teamB")
                .build();

        /**
         * 동적 쿼리는 최소값이라도 있는게 좋다. 빈 값 XX
         */

        List<MemberTeamDto> memberTeamDtos = preMemberJpaRepository.search(condition);

        assertThat(memberTeamDtos).extracting("username").containsExactly("member4");
    }

    // ----------------------------------------- 동적 쿼리와 성능 최적화 조회 - ⭐️Where 다중 파라미터 사용 끝 ⭐️-----------------------------------------
}