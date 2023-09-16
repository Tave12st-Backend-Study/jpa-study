package reviewjpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();
        // code 시작

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Member memberA = em.find(Member.class, 1L);
            Member memberB = em.find(Member.class, 1L);
            Member memberC = em.find(Member.class, 1L);

            em.persist(memberA);
            em.persist(memberB);
            em.persist(memberC);


            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);
            List<Member> members = query.getResultList();


            System.out.println("--------------------");

            tx.commit(); // 성공하면 커밋

        } catch (Exception e) {
            tx.rollback(); // 실패하면 롤백
        } finally {
            em.close();
        }

        emFactory.close();
        // code 끝
    }
}
