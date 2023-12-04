package querydsl.querydsl;


import static querydsl.querydsl.QuerydslApplicationTests.generateMember;
import static querydsl.querydsl.QuerydslApplicationTests.generateTeam;
import static querydsl.querydsl.domain.QMember.member;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import querydsl.querydsl.domain.Member;
import querydsl.querydsl.domain.Team;

@SpringBootTest
@Transactional
public class QuerydslEssentialsTest {

    /**
     * 중급 문법
     */

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

    // ----------------------------------------- 프로젝션 결과 반환 - 기본 -----------------------------------------

    @Test
    @DisplayName("프로잭션이 1개일 경우")
    void simpleProjection() {
        List<String> result = queryFactory
                .select(member.username)
                .from(member)
                .fetch();

        for (String s : result) {
            System.out.println("s = " + s);
        }
    }

    @Test
    void tupleProjection() {

        /** 값의 반환 타입이 여러개이므로 Tuple 형태
         * Tuple 은 QueryDsl 전용 타입이다.
         * 이러한 것이 repository까지는 와도 되는데, service까지 넘어가는 것은 좋지 않다.
         * 하부 구현 기술이 앞단이 알면 좋지 않음.
         */

        List<Tuple> result = queryFactory
                .select(member.username, member.age)
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            String username = tuple.get(member.username);
            Integer age = tuple.get(member.age);

            System.out.println("username = " + username);
            System.out.println("age = " + age);
        }
    }

    // ----------------------------------------- 프로젝션 결과 반환 - 기본 끝 -----------------------------------------



}