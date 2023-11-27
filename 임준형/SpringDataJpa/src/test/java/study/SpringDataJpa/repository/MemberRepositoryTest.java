package study.SpringDataJpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.SpringDataJpa.entity.Member;
import study.SpringDataJpa.entity.Team;
import study.SpringDataJpa.repository.dto.MemberDto;
import study.SpringDataJpa.repository.projections.UserNameOnly;
import study.SpringDataJpa.repository.projections.UserNameOnlyDto;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;
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

    @Test
    void returnType() {

        Member member1 = Member.builder()
                .username("userA")
                .age(13)
                .build();

        Member member2 = Member.builder()
                .username("userB")
                .age(33)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> byNames = memberRepository.findListByUsername("userA");
        List<Member> canEmptyList = memberRepository.findListByUsername("userAsadfasdfadsfzvcx");
        // canEmptyList는 비어있는 empty List
        byNames.forEach(System.out::println);
        Member userA = memberRepository.findMemberByUsername("userA");
        System.out.println("member = " + userA);
        Optional<Member> user = memberRepository.findOptionalByUsername("userAsadfasdfadsfzvcx");
    }

    @Test
    void paging() {
        createMember(10);

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 6, Sort.by(Direction.DESC, "username"));
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 실제 데이터.
        List<Member> content = page.getContent();
        content.forEach(System.out::println);

        int totalPages = page.getTotalPages();
        System.out.println("totalPages = " + totalPages);   // 3 - 총 페이지 개수

        long totalElements = page.getTotalElements();
        System.out.println("totalElements = " + totalElements); // 9 - 총 결과 개수

        int number = page.getNumber();
        System.out.println("number = " + number);   // 현재 페이지 수

        boolean isFirst = page.isFirst();
        System.out.println("isFirst = " + isFirst);

        boolean hasNext = page.hasNext();
        System.out.println("hasNext = " + hasNext);

        Slice<Member> slice = memberRepository.findByAge(age, pageRequest);
        int numberOfElements = slice.getNumberOfElements();
        int size = slice.getSize();
        int number1 = slice.getNumber();

        System.out.println("numberOfElements = " + numberOfElements);
        System.out.println("size = " + size);
        System.out.println("number1 = " + number1);

        Page<MemberDto> memberDtoPaging = page.map(member -> new MemberDto(member.getId(), member.getUsername(), "teamName"));

    }

    @Test
    void bulkUpdate() {
        createMember(30);

        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(20);
    }

    @Test
    void findEntityGraph() {
        createMember(30);

        List<Member> result = memberRepository.findByUsername("user7");
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }

    @Test
    void queryHint() {

        // given
        Member member1 = Member.builder()
                .username("userA")
                .age(13)
                .build();
        memberRepository.save(member1);
        em.flush();
        em.clear();
        // --- 영속성 컨텍스트는 빈상태 ---

        //when
        // 만약 화면에 뿌릴 때, 기존의 DB값이 아닌 바꿔서 뿌려주고 싶고, DB에 반영은 하기 싫을 때
        // 값이 변경되면 이를 더티체킹하고 트랜잭션 커밋시점에 자동으로 Update Query가 나가는데, DB에 반영하기 싫을 때
        em.flush();
        Member findMember = memberRepository.findReadOnlyByUsername("userA");
        // findMember.setUsername("member2");
        // 변경이된다는 것을 알아서 스냅샷으로 갖고 있지 않는다.
    }

    @Test
    void lock() {
        // given
        Member member1 = Member.builder()
                .username("userA")
                .age(13)
                .build();
        memberRepository.save(member1);
        em.flush();
        em.clear();
        // --- 영속성 컨텍스트는 빈상태 ---

        //when
        em.flush();
        List<Member> userA = memberRepository.findLockByUsername("userA");
    }

    private void createMember(int x) {
        for (int i = 1; i < x; i++) {
            Member member = Member.builder()
                    .username("user" + i)
                    .age(10 + i)
                    .build();
            memberRepository.save(member);
        }
    }

    @Test
    void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();
    }

    @Test
    void projections() {
        Member member1 = Member.builder()
                .age(20)
                .username("userA")
                .team(null)
                .build();
        Member member2 = Member.builder()
                .age(20)
                .username("userB")
                .team(null)
                .build();

        em.persist(member1);
        em.persist(member2);

        em.flush();
        em.clear();

        List<UserNameOnly> result = memberRepository.findCustomByUsername("userA");
        List<UserNameOnlyDto> result2 = memberRepository.findProjectionsByUsername("userA");

//        for (UserNameOnly userNameOnly : result) {
//            System.out.println("userNameOnly.getUsername() = " + userNameOnly.getUsername());
//        }
        for (UserNameOnlyDto userNameOnlyDto : result2) {
            System.out.println("userNameOnlyDto.getUsername() = " + userNameOnlyDto.getUsername());
        }

    }

}