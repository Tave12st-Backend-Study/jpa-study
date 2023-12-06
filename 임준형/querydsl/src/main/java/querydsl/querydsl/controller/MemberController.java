package querydsl.querydsl.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import querydsl.querydsl.dto.MemberSearchCondition;
import querydsl.querydsl.dto.MemberTeamDto;
import querydsl.querydsl.repository.MemberRepository;
import querydsl.querydsl.repository.PreMemberJpaRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final PreMemberJpaRepository preMemberJpaRepository;
    private final MemberRepository memberRepository;

    @GetMapping("v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return preMemberJpaRepository.search(condition);
    }

    @GetMapping("v2/members")
    public Page<MemberTeamDto> searchMemberV2(MemberSearchCondition condition, Pageable pageable) {
        return memberRepository.searchPageComplex(condition, pageable);
    }
}
