package jpabook_jinu.jpashop_jinu.service;

import jpabook_jinu.jpashop_jinu.domain.Member;
import jpabook_jinu.jpashop_jinu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers=memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }

    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    @Transactional
    public void update(Long id,String name){
        Member member=memberRepository.findOne(id);
        member.setName(name);
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


}

