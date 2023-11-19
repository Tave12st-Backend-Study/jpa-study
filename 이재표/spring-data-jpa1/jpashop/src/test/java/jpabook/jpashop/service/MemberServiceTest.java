package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Test
    void 회원가입()throws Exception{
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long join = memberService.join(member);

        //then
        Assert.assertEquals(member,memberRepository.findOne(join));
    }

    @Test
    void 중복_회원_예약()throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim1");
        Member member2 = new Member();
        member2.setName("kim1");

        //when
        memberService.join(member1);
        try{
            memberService.join(member2);
        }catch (IllegalArgumentException e){
            return;
        }

        //then
        Assert.fail("예외가 발생해야 한다.");
    }
}