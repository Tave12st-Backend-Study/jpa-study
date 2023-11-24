package study.querydsl.entity;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach //실행전에 돌아간다
    public void before(){
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    }

    @Test
    public void startJPQL(){
        String qlString =
                "select m from Member m" +
                " where m.username = :username";
        //member1을 찾아라.
        Member findMember = em.createQuery(qlString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl(){
        QMember m = new QMember("m");//별칭 직접 적용(같은 테이블 조인하는 경우만 사용)
        QMember m1 = member;//기본 인스턴스
        //variable은 식별용

        Member findMember = queryFactory.select(m)
                .from(m)
                .where(m.username.eq("member1")) //파라미터 바인딩
                .fetchOne();
        /*querydsl 이점 : 1. 컴파일 시점에서 오류를 발견 2. 파라미터 바인딩을 자동으로 실행*/


        //기본인스턴스는 static import 권장
        Member findMember2 = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1")) //파라미터 바인딩
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
    //and - 1) 체이닝 방법
    @Test
    public void search(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1")
                        .and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    //and는 ,로 바꿔서 사용할 수 있다. 두가지 방법
    //and - 2) 콤마로 작성하는 방법
    @Test
    public void searchAndParam(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1")
                        , member.age.eq(10)
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void resultFetch(){
        //1. member 목록 리스트 조회
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        //2.단건 조회
        Member fetchOne = queryFactory
                .selectFrom(member)
                .fetchOne();

        //3. limit(1).fetchOne() 한건 조회
        Member fetchFirst = queryFactory
                .selectFrom(member)
                .fetchFirst();

        //4. 페이징 정보 조회
        QueryResults<Member> results = queryFactory
                .selectFrom(member)
                .fetchResults();

        //5. count 수 조회
        long count = queryFactory
                .selectFrom(member)
                .fetchCount();
    }

    /*
    * 회원정렬순서
    * 1.회원 나이 내림차순
    * 2. 회원 이름 오름차순
    * 단 2에서 회원 이름이 없으면 마지막에 출력(null last)
    * */
    @Test
    public void sort(){
        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(),
                        member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member membernull = result.get(2);

        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(membernull.getUsername()).isNull();

    }

    @Test
    public void paging1(){
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1) //몇번째?
                .limit(2)
                .fetch();
        assertThat(result.size()).isEqualTo(2);
    }
}
