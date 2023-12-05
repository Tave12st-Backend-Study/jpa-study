package querydsl.querydsl.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import querydsl.querydsl.dto.MemberSearchCondition;
import querydsl.querydsl.dto.MemberTeamDto;
import querydsl.querydsl.repository.MemberJpaRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberJpaRepository memberJpaRepository;

    @GetMapping("v1/members")
    public List<MemberTeamDto> searchMemberV1(MemberSearchCondition condition) {
        return memberJpaRepository.search(condition);
    }
}
