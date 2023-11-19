package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MemberRepository extends JpaRepository<Member, Long> { //타입과 pk 타입을 작성
    //select m from Member m where m.name = ? 로 자동으로 만들어 준다.
    List<Member> findByName(String name);
}
