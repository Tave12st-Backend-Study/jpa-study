package jpabook_jinu.jpashop_jinu.repository;

import jpabook_jinu.jpashop_jinu.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> {
    List<Member> findByName(String name);

}
