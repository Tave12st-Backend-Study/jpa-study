package querydsl.querydsl;


import static querydsl.querydsl.QuerydslApplicationTests.generateMember;
import static querydsl.querydsl.QuerydslApplicationTests.generateTeam;
import static querydsl.querydsl.domain.QMember.member;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
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

    // ----------------------------------------- 프로젝션 결과 반환 - @QueryProjection 끝 -----------------------------------------


    // ----------------------------------------- 동적 쿼리 - BooleanBuilder -----------------------------------------

    @Test
    void dynamicQuery_BooleanBuilder() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember1(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    /**
     * Parameter 가 null이냐, null이 아니냐에 따라 쿼리가 동적으로 바뀌어야한다.
     * usernameCond가 Null일 경우 ageCond로만 검색해야하고,
     * ageCond가 Null일 경우 usernameCond로만 검색해야하며
     * 둘 다 null일 경우 where 조건 문이 나가지 않는 것이다.
     */

    private List<Member> searchMember1(String usernameCond, Integer ageCond) {

        BooleanBuilder builder = new BooleanBuilder();

        /**
         * 아래와 같이 필수값(초기값) 설정 또한 가능
         */
        BooleanBuilder builder2 = new BooleanBuilder(member.username.eq(usernameCond));

        if (usernameCond != null) {
            builder.and(member.username.eq(usernameCond));
        }

        if (ageCond != null) {
            builder.and(member.age.eq(ageCond));
        }

        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }

    // ----------------------------------------- 동적 쿼리 - BooleanBuilder 끝 -----------------------------------------


    // ----------------------------------------- 동적 쿼리 - ⭐️Where 다중 파라미터 사용⭐️ -----------------------------------------

    @Test
    @DisplayName("가장 직관적인 방법")
    void dynamicQuery_WhereParam() {
        String usernameParam = "member1";
        Integer ageParam = 10;

        List<Member> result = searchMember2(usernameParam, ageParam);
        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    /**
     * 여기서 queryDsl의 충격적인 엄청난 기능은, where 조건 문 속 메서드의 반환이 null이면 자동으로 무시가 된다.
     */
    private List<Member> searchMember2(String usernameCond, Integer ageCond) {
        return queryFactory
                .selectFrom(member)
//                .where(usernameEq(usernameCond), ageEq(ageCond))
                .where(allEq(usernameCond, ageCond))
                .fetch();
    }

    /**
     * 아래와 같이 조합을 원할 경우, 조합을 원하는 원소 메서드들의 반환 값을
     * Predicate가 아닌 BooleanExpression 으로 변경해야한다.
     * 기본 적으로 Where 문 매개변수 속 메서드의 반환 타입은 Predicate 이지만,
     * 아래에서 조합하기 위해 BooleanExpression로 수정, 조합 가능 및 쿼리 정상 동작
     * 단독으로 또는 조합해서 재사용 가능!
     *
     * ex)
     * 광고 상태 isValid And 날짜가 IN 이여야함 = isServicable() 조합 및 재사용 가능
     */
    private Predicate allEq(String usernameCond, Integer ageCond) {
        return usernameEq(usernameCond).and(ageEq(ageCond));
    }

    private BooleanExpression usernameEq(String usernameCond) {
        if (usernameCond != null) {
            return member.username.eq(usernameCond);
        }
        return null;
    }

    private BooleanExpression ageEq(Integer ageCond) {
        if (ageCond != null) {
            return member.age.eq(ageCond);
        }
        return null;
    }

    // ----------------------------------------- 동적 쿼리 - ⭐️Where 다중 파라미터 사용 끝 ⭐️-----------------------------------------


    // ----------------------------------------- 수정, 삭제 벌크 연산 -----------------------------------------

    @Test
    @Commit
    void bulkUpdate() {

        /**
         * 영향을 받은 카운트가 return
         */
        long count = queryFactory
                .update(member)
                .set(member.username, ("비회원"))
                .where(member.age.lt(28))
                .execute();

        /**
         * 당연하게도, 영속성 컨텍스트 잔여 쿼리 flush 및 clear
         */
        em.flush();
        em.clear();
    }

    @Test
    void bulkAdd() {
        /**
         * 모든 회원의 나이를 더하는 쿼리
         * 마이너스가 없어서 빼려면 -1을 더해야한다.
         */
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.add(1))
                .execute();
    }

    @Test
    void bulkMultiple() {
        /**
         * 모든 회원의 나이를 2배 곱하는 쿼리
         */
        long count = queryFactory
                .update(member)
                .set(member.age, member.age.multiply(2))
                .execute();
    }

    @Test
    void bulkDelete() {
        /**
         * 10살 보다 많은 나이의 회원을 삭제하는 쿼리
         */
        long count = queryFactory
                .delete(member)
                .where(member.age.gt(10))
                .execute();
    }

    // ----------------------------------------- 수정, 삭제 벌크 연산 끝 -----------------------------------------
}