package reviewjpa;

import reviewjpa.superclass.Item;
import reviewjpa.superclass.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();
        // code 시작

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            em.flush();
            em.clear();

            // 영속성 컨텍스트에 존재
            Member findM = em.find(Member.class, member1.getId());
            Member referenceM = em.getReference(Member.class, member1.getId());

            System.out.println("----- 영속성 컨텍스트에 이미 존재하면 프록시가 아닌 엔티티를 반환 -----");
            System.out.println("findM.getClass() = " + findM.getClass());
            System.out.println("referenceM.getClass() = " + referenceM.getClass());
            
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
