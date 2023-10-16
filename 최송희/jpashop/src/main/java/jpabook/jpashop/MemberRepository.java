package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
/*
* Component 스캔 대상 -> 자동 스프링 빈 등록
* */
public class MemberRepository {
    @PersistenceContext
    private EntityManager em; //스프링 부트에서는 엔티티 매니저를 생성 (spring-boot-data-jpa)

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }
    /*
    * command와 query를 분리해라 - id만 반환하도록
    * */

    public Member find(Long id){
        return em.find(Member.class, id);
    }

}
