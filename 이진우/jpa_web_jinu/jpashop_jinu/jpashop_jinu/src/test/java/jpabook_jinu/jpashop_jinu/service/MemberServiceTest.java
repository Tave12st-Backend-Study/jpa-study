package jpabook_jinu.jpashop_jinu.service;

import jpabook_jinu.jpashop_jinu.domain.Member;
import jpabook_jinu.jpashop_jinu.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception{
        //given
        Member member=new Member();
        member.setName("kim");

        //when
        Long saveId=memberService.join(member);
        //then

        Assertions.assertThat(member.getId()).isEqualTo(saveId);
    }

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1=new Member();
        member1.setName("kim");
        Member member2=new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        try{
            memberService.join(member2);

        }catch (IllegalStateException e){
           return;
        }

        //then
        fail("예외가 발생햐아 한다");

    }

}