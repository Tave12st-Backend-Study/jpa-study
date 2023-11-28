package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class HelloController {

    private final MemberRepository memberRepository;


    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping("/members")
    public Page<Member> list(Pageable pageable){
        Page<Member> page=memberRepository.findAll(pageable);

        return page;
    }

    @GetMapping("/members-dto")
    public Page<MemberDto> list2(Pageable pageable){
        Page<Member> page=memberRepository.findAll(pageable);
        Page<MemberDto> pageDto=page.map(MemberDto::new);

        return pageDto;
    }



    @PostConstruct
    public void init(){
        for(int i=0;i<100;i++){
            memberRepository.save(new Member("user"+i,i));
        }
    }
}
