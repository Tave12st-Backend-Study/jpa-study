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

            Member referenceM = em.getReference(Member.class, member1.getId());
            System.out.println("referenceM.getClass() = " + referenceM.getClass()); // proxy

            // 준영속 상태로 변경
            System.out.println("----- 영속성 컨텍스트에서 제외시 접근 할 수 없음 -----");
            em.detach(referenceM);

            System.out.println("-----\n " +
                    "여기서 오류 발생 \n" +
                    "could not initialize proxy [reviewjpa.Member#1] - no Session \n" +
                    "-----");
            System.out.println("referenceM.getUsername() = " + referenceM.getUsername());

            tx.commit(); // 성공하면 커밋

        } catch (Exception e) {
            tx.rollback(); // 실패하면 롤백
            e.printStackTrace();
        } finally {
            em.close();
        }

        emFactory.close();
        // code 끝
    }

}
