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

            // 조회 결과가 하나 이상일 경우
            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            List<Member> resultList = query1.getResultList();

            for (Member member1 : resultList) {
                System.out.println("member1 = " + member1);
            }

            // 조회 결과가 하나일 경우
            TypedQuery<Member> query2 = em.createQuery("select m from Member m where m.id = 1L", Member.class);
            Member singleResult = query2.getSingleResult();

            System.out.println("singleResult = " + singleResult);

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
