package reviewjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();
        // code 시작

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Member member = em.find(Member.class, 1L);
            em.persist(member);

            // commit 시점에 자동으로 flush가 되지만,
            // 미리 DB에 넘어가는 쿼리를 보고 싶거나 미리 DB에 저장하고 싶을 때 사용함
            em.flush();

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
