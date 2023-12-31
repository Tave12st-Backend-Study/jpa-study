package jpashop.realspringjpa2.repository;

import java.util.List;
import javax.persistence.EntityManager;
import jpashop.realspringjpa2.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PreMemberRepository {

    // spring에서 @PersistContext를 의존성 주입처럼 @Autowired도 지원을 해준다.
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m From Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m From Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
