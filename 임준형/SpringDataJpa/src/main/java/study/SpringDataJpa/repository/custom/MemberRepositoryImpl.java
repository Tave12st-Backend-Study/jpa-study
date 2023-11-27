package study.SpringDataJpa.repository.custom;

import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import study.SpringDataJpa.entity.Member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{


    private final EntityManager em;

    // mybatis 등 다른 것들 전부 가능
    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m").getResultList();
    }
}
