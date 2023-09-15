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
        EntityTransaction transaction = em.getTransaction();
        transaction.begin();

        Member member = em.find(Member.class, 1L);
        member.setName("update");
        System.out.println("==================");

        //커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
        transaction.commit(); // [트랜잭션] 커밋

        em.close();
        emf.close();
    }
}
