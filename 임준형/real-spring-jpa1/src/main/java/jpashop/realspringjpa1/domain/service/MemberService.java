package jpashop.realspringjpa1.domain.service;

import java.util.List;
import jpashop.realspringjpa1.domain.Member;
import jpashop.realspringjpa1.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicatedMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     *
     * @Transactional(readOnly = true) 설정
     * 영속성 컨텍스트의 flush를 하지 않으며, 더티체킹을 하지 않아서 최적화를 자동으로 해줌
     */

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    // 회원 단건 조회

    public Member findOne(Long id) {
        return memberRepository.findOne(id);
    }

    public void validateDuplicatedMember(Member member) {
        boolean exists = memberRepository.findByName(member.getUsername())
                .stream()
                .anyMatch(m -> m.getUsername().equals(member.getUsername()));

        if (exists) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

}
