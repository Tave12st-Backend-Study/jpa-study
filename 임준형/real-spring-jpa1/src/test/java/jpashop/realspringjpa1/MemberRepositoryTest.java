package jpashop.realspringjpa1;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional  // test에 있으면 실행 후 자동 rollback
//    @Rollback(value = false)
    void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);

        //then
        Member findMember = memberRepository.find(saveId);

        Assertions.assertThat(findMember.getId().equals(member.getId()));

//        당연하게도, 영속성 컨텍스트에 존재하니 True
        Assertions.assertThat(findMember).isEqualTo(member);

    }

}