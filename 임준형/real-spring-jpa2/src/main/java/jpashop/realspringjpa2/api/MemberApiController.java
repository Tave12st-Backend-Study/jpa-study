package jpashop.realspringjpa2.api;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import jpashop.realspringjpa2.api.dto.CreateMemberRequest;
import jpashop.realspringjpa2.api.dto.CreateMemberResponse;
import jpashop.realspringjpa2.api.dto.MemberDto;
import jpashop.realspringjpa2.api.dto.Result;
import jpashop.realspringjpa2.api.dto.UpdateMemberRequest;
import jpashop.realspringjpa2.api.dto.UpdateMemberResponse;
import jpashop.realspringjpa2.domain.Member;
import jpashop.realspringjpa2.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;


    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> members = memberService.findMembers();
        List<MemberDto> list = members.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        int size = list.size();
        return new Result(size, list);
    }



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

    @PatchMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        String name = request.getName();
        Member updateMember = memberService.update(id, name);

        return new UpdateMemberResponse(updateMember.getId(), updateMember.getName());
    }
}
