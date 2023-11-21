package study.SpringDataJpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.SpringDataJpa.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
