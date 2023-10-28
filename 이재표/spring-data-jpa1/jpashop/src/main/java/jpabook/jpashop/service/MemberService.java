package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    /*
        회원 가입
         */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member);//중복 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getUsername());
        if(!findMembers.isEmpty()){
            throw new IllegalArgumentException("이미 존재하는 회원입니다.");
        }
    }

    public List<Member>findMembers(){
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
