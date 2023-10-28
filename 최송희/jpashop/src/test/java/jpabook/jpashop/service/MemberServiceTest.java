package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given 이렇게 주어졌을때
        Member member = new Member();
        member.setName("kim");

        //when 이렇게 되면
        Long savedId = memberService.join(member);

        //then 다음의 결과
        assertEquals(member, memberService.findOne(savedId));
    }

    /*
    * 기본적으로 Test class에서의 @Transactional로 인한 Rollback
    * ::
    * - 테스트 코드에서는 기본적으로 메서드 실행 후 롤백이 된다.
    * + insert 쿼리 실행X(기본적으로 트랜잭션 커밋 전에 flush 발생, 영속성 컨텍스트의 DB 반영)
    * + DB에 값 저장x
    *
    * - 테스트 코드의 특정 메서드에서 롤백이 되지 않도록 하려면 @Rollback(false) 를 메서드 단에 추가
    * + insert 쿼리 실행O
    * + DB에 값 저장O
    * */

    @Test
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim1");

        Member member2 = new Member();
        member2.setName("kim1");
        //when
        memberService.join(member1);
        try{
            memberService.join(member2);
        }catch(IllegalStateException e){
            return;
        }
        //then
        Assertions.fail("예외가 발생해야 합니다.");

    }
}