package querydsl.querydsl.repository;

import static querydsl.querydsl.domain.QMember.member;
import static querydsl.querydsl.domain.QTeam.team;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;
import querydsl.querydsl.dto.MemberSearchCondition;
import querydsl.querydsl.dto.MemberTeamDto;
import querydsl.querydsl.dto.QMemberTeamDto;

// 이곳의 클래스 이름이 중요함
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<MemberTeamDto> search(MemberSearchCondition condition) {
        return jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        userAgeGoe(condition.getAgeGoe()),
                        userAgeLoe(condition.getAgeLoe())
                )
                .fetch();
    }

    private BooleanExpression usernameEq(String username) {
        if (StringUtils.hasText(username)) {
            return member.username.eq(username);
        }
        return null;
    }

    private BooleanExpression teamNameEq(String teamName) {
        if (StringUtils.hasText(teamName)) {
            return team.name.eq(teamName);
        }
        return null;
    }

    private BooleanExpression userAgeGoe(Integer ageGoe) {
        if (ageGoe != null) {
            return member.age.goe(ageGoe);
        }
        return null;
    }

    private BooleanExpression userAgeLoe(Integer ageLoe) {
        if (ageLoe != null) {
            return member.age.loe(ageLoe);
        }
        return null;
    }

    @Override
    public Page<MemberTeamDto> searchPageComplex(MemberSearchCondition condition, Pageable pageable) {

        List<MemberTeamDto> content = getSearchPageContent(condition, pageable);

        /**
         * Count Query가 생략 가능한 경우
         * 1. 페이지 시작이면서 컨텐츠 사이즈가 페이지 사이즈보다 작을 때
         * 2. 마지막 페이지일 때 (offset + 컨텐츠 사이즈를 더해서 전체 사이즈 구함)
         */

        JPAQuery<Long> countQuery = getSearchPageCount(condition);

//        return new PageImpl<>(content, pageable, count);
//        count Query를 자동으략로 최적화
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private JPAQuery<Long> getSearchPageCount(MemberSearchCondition condition) {
        return jpaQueryFactory
                .select(Wildcard.count)
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        userAgeGoe(condition.getAgeGoe()),
                        userAgeLoe(condition.getAgeLoe())
                );
    }

    private List<MemberTeamDto> getSearchPageContent(MemberSearchCondition condition, Pageable pageable) {
        return jpaQueryFactory
                .select(new QMemberTeamDto(
                        member.id.as("memberId"),
                        member.username,
                        member.age,
                        team.id.as("teamId"),
                        team.name.as("teamName")
                ))
                .from(member)
                .leftJoin(member.team, team)
                .where(
                        usernameEq(condition.getUsername()),
                        teamNameEq(condition.getTeamName()),
                        userAgeGoe(condition.getAgeGoe()),
                        userAgeLoe(condition.getAgeLoe())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

}
