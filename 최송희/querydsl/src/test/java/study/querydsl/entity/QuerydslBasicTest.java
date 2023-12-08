package study.querydsl.entity;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.VisibleForTesting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.*;

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
                .offset(1) //몇번째?를 조회할건지(0부터 시작)
                .limit(2)
                .fetch();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    public void aggregation(){
        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.sum(),
                        member.age.avg(),
                        member.age.max(),
                        member.age.min()
                ).from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);
        assertThat(tuple.get(member.age.sum())).isEqualTo(100);
        assertThat(tuple.get(member.age.avg())).isEqualTo(25);

    }

    /*
    팀의 이름과 각팀의 평균 연령을 구해라
     */
    @Test
    public void group() throws Exception{
        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);

        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);
    }

    /*
    * 팀A에 소속된 모든 회원을 찾아라
    * */
    @Test
    public void join() throws Exception{
        //given
        List<Member> result = queryFactory
                .selectFrom(member)
                .join(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();

        assertThat(result)
                .extracting("username")
                .containsExactly("member1", "member2");

        //left join
        List<Member> leftJoinResult = queryFactory
                .selectFrom(member)
                .leftJoin(member.team, team)
                .where(team.name.eq("teamA"))
                .fetch();
    }

    /*
    * 세타조인
    * 회원의 이름과 팀 이름과 같은 회원 조회(연관관계 없는 테이블을 조인하는 경우)
    * */
    @Test
    public void theta_join() throws Exception{
        //given
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        //when
        List<Member> result = queryFactory
                .select(member)
                .from(member, team)
                .where(member.username.eq(team.name))
                .fetch();

        //then
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    /*
    * 예) 회원과 팀을 조인하면서, 팀 이름이 teamA인 팀만 조인, 회원은 모두 조회
    * JPQL : select m, t from Member m left join m.team t on t.name = "teamA"
    * */
    @Test
    public void join_on_filtering() throws Exception{
        //given
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("teamA"))
                .fetch();
        //위는 외부조인으로, leftjoin이므로 teamA인 회원과 teamA가 아닌 회원까지 모두 조회된다.
        //ex. teamA인 회원정보와 teamB인 회원은 team정보가 null로 표현

        List<Tuple> result2 = queryFactory
                .select(member, team)
                .from(member)
                .join(member.team, team).on(team.name.eq("teamA"))
                .fetch();
        //위는 내부조인으로, teamA인 회원 정보만 조회된다.
        // on대신 where로 써도 똑같다.
    }

    /*
     * 연관관계가 없는 엔티티를 외부조인
     * 회원의 이름이 팀 이름과 같은 대상 조회
     * */
    @Test
    public void join_on_no_relation() throws Exception{
        //given
        em.persist(new Member("teamA"));
        em.persist(new Member("teamB"));
        em.persist(new Member("teamC"));

        //when
        List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(team).on(member.username.eq(team.name))
                .fetch();
        // 보통 조인할 때   leftjoin(member.team, team)이렇게 한다. 그러면 id값을 기준으로 필터링해준다.
        // 하지만 여기서는 엔티티 하나만 적고 leftJoin(team)을 썻다. on절에 맞지 않는 정보는 null로 나오고 on절에 맞는 정보는 모두 나온다.

        //then
        assertThat(result)
                .extracting("username")
                .containsExactly("teamA", "teamB");
    }

    @PersistenceUnit
    EntityManagerFactory emf;
    //페치조인-연관된 엔티티를 SQL 한번에 조회하는 기능(성능 최적화)
    @Test
    public void fetchJoinNo() throws Exception{
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        //LAZY로 페치전략을 사용해서 TEAM은 조회되지 않는다.

        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치조인 미적용").isFalse();
        //team이 로딩이 되어서는 안된다.
    }

    @Test
    public void fetchJoinUse() throws Exception{
        Member findMember = queryFactory
                .selectFrom(member)
                .join(member.team, team).fetchJoin() //페치조인
                .where(member.username.eq("member1"))
                .fetchOne();


        boolean loaded = emf.getPersistenceUnitUtil().isLoaded(findMember.getTeam());
        assertThat(loaded).as("페치조인 미적용").isTrue();
        //team이 로딩된다.
    }

    /*
    * 나이가 가장 많은 회원 조회
    *
    * */
    @Test
    public void subQuery() throws Exception{

        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(
                        JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(40);
    }


    /*
     * 나이가 평균이상인 회원 조회
     *
     * */
    @Test
    public void subQueryGoe() throws Exception{
        //whre절 서브쿼리
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.goe(
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub)
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(30, 40); //30살, 40살 두개
    }

    /*
     * 나이가 평균이상인 회원 조회
     *
     * */
    @Test
    public void subQueryIn() throws Exception{
        //whre절 서브쿼리
        QMember memberSub = new QMember("memberSub");
        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.in(
                        JPAExpressions
                                .select(memberSub.age)
                                .from(memberSub)
                                .where(memberSub.age.gt(10)) //10살 초과
                ))
                .fetch();

        assertThat(result).extracting("age")
                .containsExactly(20, 30, 40);
    }

    @Test
    public void selectSubQuery(){
        QMember memberSub = new QMember("memberSub");
        //select절 서브쿼리
        List<Tuple> result = queryFactory
                .select(member.username,
                        JPAExpressions
                                .select(memberSub.age.avg())
                                .from(memberSub))
                .from(member)
                .fetch();

        for (Tuple tuple : result) {
            System.out.println("Tuple:"+tuple);
        }

    }

    @Test
    public void basicCase(){
        List<String> fetch = queryFactory
                .select(member.age
                        .when(10).then("열살")
                        .when(20).then("스무살")
                        .otherwise("기타"))
                .from(member)
                .fetch();
    }

    //최대한 프레젠테이션 로직에서 해결하도록..
    @Test
    public void complexCase(){
        List<String> fetch = queryFactory
                .select(new CaseBuilder()
                        .when(member.age.between(0, 20)).then("0~20살")
                        .when(member.age.between(21, 30)).then("21~30살")
                        .otherwise("기타"))
                .from(member)
                .fetch();

    }
}
