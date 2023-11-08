package jpabook_jinu.jpashop_jinu.service;

import jpabook_jinu.jpashop_jinu.domain.Member;
import jpabook_jinu.jpashop_jinu.repository.MemberRepositoryOld;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(SpringExtension.class)
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired
    MemberRepositoryOld memberRepository;


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
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        //then
        assertEquals("이미 존재하는 회원입니다", thrown.getMessage());

    }

}