package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Member memberA = new Member(3L, "memberA");
        Member memberB = new Member(4L, "memberB");
        Member memberC = new Member(5L, "memberC");

        em.persist(memberA);
        em.persist(memberB);
        em.persist(memberC);
        //중간에 JPQL 실행
        List<Member> result = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        tx.commit();

        em.close();
        emf.close();
    }
}
