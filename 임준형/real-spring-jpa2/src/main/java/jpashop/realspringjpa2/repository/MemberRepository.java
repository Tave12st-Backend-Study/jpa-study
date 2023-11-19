package jpashop.realspringjpa2.repository;

import java.util.List;
import jpashop.realspringjpa2.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name);
}
