package jpql;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            // 엔티티 프로젝션
            List<Member> result1 = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = result1.get(0);
            findMember.setAge(20);

            // 임베디드 프로젝션
            List<Address> result2 = em.createQuery("select o.address from Order o", Address.class)
                    .getResultList();

            // 스칼라 타입 프로젝션
            List<Address> result3 = em.createQuery("select distinct m.username, m.age from Member m")
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
