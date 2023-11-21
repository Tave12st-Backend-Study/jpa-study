package study.SpringDataJpa.repository;

import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import study.SpringDataJpa.entity.Member;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

//    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
