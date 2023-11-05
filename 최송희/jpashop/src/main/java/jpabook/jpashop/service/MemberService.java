package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//기본적으로 트랜잭션안에서 데이터의 변경이 이뤄져야 한다. -- public 메서드가 이 안에서 이뤄지게 된다.
@RequiredArgsConstructor
public class MemberService {

    //대안 1
    private final MemberRepository memberRepository;

    /*
    대안 2

    생성자 주입
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
     this.memberRepository = memberRepository;
    }
    * */


    //회원 가입
    @Transactional
    //readOnly = false가 적용됨
    public Long join(Member member){
        validateDuplicateMember(member); //중복회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member){
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());

        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public void update(Long id, String name){
        Member member = findOne(id);
        member.setName(name);
    }


}
