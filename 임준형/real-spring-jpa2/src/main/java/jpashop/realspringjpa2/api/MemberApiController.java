package jpashop.realspringjpa2.api;

import javax.validation.Valid;
import jpashop.realspringjpa2.api.dto.CreateMemberRequest;
import jpashop.realspringjpa2.api.dto.CreateMemberResponse;
import jpashop.realspringjpa2.domain.Member;
import jpashop.realspringjpa2.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    // 엔티티를 절대 외부로 노출하지 말것
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long joinId = memberService.join(member);
        return new CreateMemberResponse(joinId);
    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long joinId = memberService.join(member);
        return new CreateMemberResponse(joinId);
    }
}
