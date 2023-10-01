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
            Member findM = em.find(Member.class, member1.getId());

            System.out.println("----- 처음에 프록시로 반환하면, 그 후에도 프록시로 반환 -----");
            System.out.println("findM.getClass() = " + findM.getClass());
            System.out.println("referenceM.getClass() = " + referenceM.getClass());

            System.out.println("----- JPA는 컬렉션처럼 비교 연산시 같아야하므로 reference와 find를 같게 연산 함 -----");
            System.out.println("(findM == referenceM) = " + (findM == referenceM));
            
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
