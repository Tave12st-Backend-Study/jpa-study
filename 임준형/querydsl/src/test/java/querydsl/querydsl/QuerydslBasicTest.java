package querydsl.querydsl;

import static org.assertj.core.api.Assertions.assertThat;
import static querydsl.querydsl.QuerydslApplicationTests.generateMember;
import static querydsl.querydsl.QuerydslApplicationTests.generateTeam;
import static querydsl.querydsl.domain.QMember.member;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydsl.domain.Member;
import querydsl.querydsl.domain.QMember;
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
}
