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

            System.out.println("프록시 인스턴스 초기화 여부 확인 = " + emFactory.getPersistenceUnitUtil().isLoaded(referenceM));
            System.out.println("----- referenceM.getUsername()으로 초기화 -----");
            referenceM.getUsername();
            System.out.println("프록시 인스턴스 초기화 여부 확인 = " + emFactory.getPersistenceUnitUtil().isLoaded(referenceM));


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
