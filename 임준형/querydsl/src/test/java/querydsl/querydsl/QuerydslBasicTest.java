package querydsl.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static querydsl.querydsl.QuerydslApplicationTests.generateMember;
import static querydsl.querydsl.QuerydslApplicationTests.generateTeam;
import static querydsl.querydsl.domain.QMember.member;
import static querydsl.querydsl.domain.QTeam.team;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydsl.domain.Member;
import querydsl.querydsl.domain.QMember;
import querydsl.querydsl.domain.QTeam;
import querydsl.querydsl.domain.Team;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
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

    @Test
    void startJPQL() {
        // member1 찾기
        String qlString = "select m from Member m "
                + "where m.username = :username";

        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQuerydsl() {

        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }


    // ----------------------------------------- 검색 조건 쿼리 -----------------------------------------
    @Test
    void search_equal() {
        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq("member1")
                        .and(QMember.member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search_between() {
        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(QMember.member.username.eq("member1")
                        .and(QMember.member.age.between(10, 30)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search_instanceAnd() {
        Member findMember = queryFactory
                .selectFrom(QMember.member)
                .where(
//                        QMember.member.username.eq("member1").and(QMember.member.age.between(10, 30)))
//                         아래와 같이 ','이 and와 같은 것이다.
                        QMember.member.username.eq("member1"), (QMember.member.age.between(10, 30)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    // ----------------------------------------- 검색 조건 쿼리 끝 -----------------------------------------


    // ----------------------------------------- 검색 결과 -----------------------------------------
    @Test
    void resultFetch() {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        /**
         * fetchFirst == limit(1).fetchOne
         */
        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        /**
         * 강의에서 나오는 fetchResult는 실제로 사용하지 않음
         * 아래와 같이 count query를 별도로 날리는게 좋음
         * 기존의 fetchCount 또한 아래로 대체
         */

        Long total = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();
    }

    // ----------------------------------------- 검색 결과 끝 -----------------------------------------


    // ----------------------------------------- 정렬 -----------------------------------------

    /**
     * 회원 정렬 순서
     * 1. 회원 나이 내림차순 (desc)
     * 2. 회원 이름 올림차순 (asc)
     * 단 2에서 회원 이름이 없으면 마지막에 출력(nulls last)
     */

    @Test
    void sort() {
        em.persist(generateMember(null, 100, null));
        em.persist(generateMember("member5", 100, null));
        em.persist(generateMember("member6", 100, null));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                // 아래와 같이 nullsFirst 또한 가능
//                .orderBy(member.age.desc(), member.username.asc().nullsFirst())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isNull();
    }

    // ----------------------------------------- 정렬 끝 -----------------------------------------


    // ----------------------------------------- 페이징 -----------------------------------------
    @Test
    void paging() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        for (Member currentMember : result) {
            System.out.println("currentMember = " + currentMember);
        }

        Long count = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        System.out.println("count = " + count);
        assertThat(result.size()).isEqualTo(2);
    }
    // ----------------------------------------- 페이징 끝 -----------------------------------------


    // ----------------------------------------- 집합 -----------------------------------------
    @Test
    void aggregation() {
        /**
         * Data Type이 여러개일 경우 Tuple을 사용
         * 실무에선 해당 방법으로 사용하지 않음, dto로 사용하는 법 후에 배움
         */
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.max())).isEqualTo(40);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);
        assertThat(tuple.get(member.age.min())).isEqualTo(10);

    }

    /**
     * 팀의 이름과 각 팀의 평균 연령 구하기
     */
    @Test
    void groupBy() {
        List<Tuple> result = queryFactory
                .select(
                        team.name,
                        member.age.avg()
                )
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
    }

    // ----------------------------------------- 집합 끝 -----------------------------------------


    // ----------------------------------------- 조인 - 기본 조인 -----------------------------------------

    /**
     * team A에 소속된 모든 회원
     */

    @Test
    void join() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamB"))
                .fetch();

        for (Member currentMember : result) {
            System.out.println("-----" + currentMember.getUsername());
        }

        assertThat(result)
                .extracting("username")
                .containsExactly("member3", "member4");
    }

    @Test
    void left_join() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(team.name.eq("teamB"))
                .fetch();

        for (Member currentMember : result) {
            System.out.println("-----" + currentMember);
        }

        assertThat(result)
                .extracting("username")
                .containsExactly("member3", "member4");
    }

    @Test
    void theta_join() {
        /**
         * 억지성 예제, 회원의 이름이 팀의 이름과 같은 회원을 조회
         */
        em.persist(generateMember("teamA", 7, null));
        em.persist(generateMember("teamB", 13, null));
        em.persist(generateMember("teamC", 31, null));

        List<Member> result = queryFactory
                .select(member)
                // 세타조인이므로 단순히 나열임
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        for (Member currentMember : result) {
            System.out.println("currentMember = " + currentMember);
        }

        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    // ----------------------------------------- 조인 - 기본 조인 끝 -----------------------------------------

    // ----------------------------------------- 조인 - on 절 -----------------------------------------
    /**
     * 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
     */
    @Test
    void join_on_filtering() {
        /**
         * leftJoin 일 때만 on으로 조인 조건을 줄일 수 있다.
         * 일반 join 일 때는 where로 할 것
         */
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team)
                .on(team.name.eq("teamA"))
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("tuple = " + tuple);
        }
    }

    @Test
    void join_on_no_relation() {
        /**
         * 연관관계까 없는 엔티티를 외부조인
         * 회원의 이름과 팀의 이름과 같은 회원을 조회
         */
        em.persist(generateMember("teamA", 7, null));
        em.persist(generateMember("teamB", 13, null));
        em.persist(generateMember("teamC", 31, null));

        List<Tuple> result = queryFactory
                .select(member, team)
                // 세타조인이므로 단순히 나열임
                .from(member)
                .leftJoin(team)
                .on(member.username.eq(team.name))
                .where(member.username.eq(team.name))
                .fetch();

        /**
         * 아까 위에서 leftjoin 할 때 member.team 을 leftJoin 에 파라미터로 넣고 안넣고의 차이가
         * member.team.id = team.id 와 같은 것이다.
         *
         * 일반 조인: leftJoin(member.team, team)
         * on 조인: from(member).leftJoin(team).on(xxx)
         * 내가 원하는 조건이 on에 들어감
         */
    }

    // ----------------------------------------- 조인 - on 절 끝 -----------------------------------------


    // ----------------------------------------- 조인 - fetch join -----------------------------------------

    @PersistenceUnit
    EntityManagerFactory emf;

    @Test
    void noFetchJoin() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        /**
         * 이미 로딩이 됬는지 가르쳐주는 메서드
         * 지연 로딩이기 때문에 당연히 Team이 로딩이 되면 안됨
         */
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 미적용").isFalse();
    }

    @Test
    void useFetchJoin() {
        em.flush();
        em.clear();

        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.username.eq("member1"))
                .fetchOne();

        /**
         * 이미 로딩이 됬는지 가르쳐주는 메서드
         * 지연 로딩이기 때문에 당연히 Team이 로딩이 되면 안됨
         */
        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치 조인 적용").isTrue();
    }

    // ----------------------------------------- 조인 - fetch join 끝 -----------------------------------------
}
