package study.SpringDataJpa.controller;

import java.util.List;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.SpringDataJpa.entity.Member;
import study.SpringDataJpa.repository.MemberRepository;
import study.SpringDataJpa.repository.dto.MemberDto;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    // 위 과정을 컨버터처럼 자동으로 진행해줌
    // 실무 권장 X
    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) {
        return member.getUsername();
    }

//    @GetMapping("/members")
//    public Page<Member> list(
//            @PageableDefault(size = 5, sort = "username") Pageable pageable) {
//        return memberRepository.findAll(pageable);
//    }

    @GetMapping("/members")
    public Page<MemberDto> list(
            @PageableDefault(size = 5, sort = "username") Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
//        return page.map(MemberDto::new); 둘 다 가능
        return page.map(MemberDto::toMemberDto);
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(Member.builder()
                    .username("user" + i)
                    .age(i)
                    .team(null)
                    .build());
        }
    }

}
