package querydsl.querydsl;


import static querydsl.querydsl.QuerydslApplicationTests.generateMember;
import static querydsl.querydsl.QuerydslApplicationTests.generateTeam;
import static querydsl.querydsl.domain.QMember.member;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
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
import querydsl.querydsl.domain.QMember;
import querydsl.querydsl.domain.Team;
import querydsl.querydsl.dto.MemberDto;
import querydsl.querydsl.dto.QMemberDto;
import querydsl.querydsl.dto.UserDto;

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


    // ----------------------------------------- 프로젝션 결과 반환 - DTO 사용 -----------------------------------------

    @Test
    @DisplayName("너무 불편한 상황, 패키지를 직접 적어야함. 이를 queryDsl이 해결")
    void findDtoByJpql() {
//        List<MemberDto> resultList = em.createQuery("select new querydsl.querydsl.dto.MemberDto(m.username, m.age) "
//                        + "from Member m", MemberDto.class)
//                .getResultList();

//        for (MemberDto memberDto : resultList) {
//            System.out.println("memberDto = " + memberDto);
//        }
    }

    @Test
    @DisplayName("역시나 setter는 사용하면 안됨, DTO가 기본샌성자가 필수이기 때문에 더더욱 별로")
    void findDtoBySetter() {
        List<MemberDto> result = queryFactory
                .select(
                        Projections.bean(MemberDto.class, member.username, member.age)
                )
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("getter, setter가 없어도 되는 fileds 접근법")
    void findDtoByField() {
        List<MemberDto> result = queryFactory
                .select(
                        Projections.fields(MemberDto.class, member.username, member.age)
                )
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("getter, setter, 기본 생성자가 없어도 됨, 생성자는 객체 필드 타입만 맞다면 ")
    void findDtoByConstructor() {
        List<MemberDto> result = queryFactory
                .select(
                        Projections.constructor(MemberDto.class, member.username, member.age)
                )
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }

    @Test
    @DisplayName("나이가 가장 많은 나이로 모두 출력, 앨리어스를 사용, 서브쿼리 사용")
    void findUserDto() {

        /**
         * MemberDto 와 UserDto 의 필드가 username, name으로 같지 않기 떄문에
         * userDto = UserDto(name=null, age=10) 이렇게 결과가 나오게 된다.
         *
         * 그렇기 떄문에 별칭으로 이름을 맞춰주면 이를 해결할 수 있다.
         * userDto = UserDto(name=member1, age=10)
         */

        QMember memberSub = new QMember("memberSub");
        List<UserDto> result = queryFactory
                .select(
                        Projections.fields(UserDto.class,
                                member.username.as("name"),
                        // 가장 많은 나이로 갖고 왔으므로 앨리어스를 필수로 두어야함.
                        ExpressionUtils.as(JPAExpressions
                                .select(memberSub.age.max())
                                .from(memberSub), "age")
                        ))
                .from(member)
                .fetch();

        for (UserDto userDto : result) {
            System.out.println("userDto = " + userDto);
        }
    }

    // ----------------------------------------- 프로젝션 결과 반환 - DTO 사용 끝 -----------------------------------------


    // ----------------------------------------- 프로젝션 결과 반환 - @QueryProjection -----------------------------------------

    @Test
    void findDtoByQueryProjection() {
        List<MemberDto> result = queryFactory
                .select(new QMemberDto(member.username, member.age))
                .from(member)
                .fetch();

        for (MemberDto memberDto : result) {
            System.out.println("memberDto = " + memberDto);
        }
    }
}