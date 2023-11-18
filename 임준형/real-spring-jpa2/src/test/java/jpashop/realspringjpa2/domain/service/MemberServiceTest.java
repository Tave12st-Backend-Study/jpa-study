package jpashop.realspringjpa2.domain.service;


import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import jpashop.realspringjpa2.domain.Member;
import jpashop.realspringjpa2.repository.MemberRepository;
import jpashop.realspringjpa2.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

//    @Rollback(value = false)
    @Test
    void 회원가입() throws Exception {

        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long join = memberService.join(member);

        //then
        Assertions.assertThat(join.equals(member.getId()));
    }

    @Test
    void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2);
        });

    }

}