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

        //엔티티 매니저는 데이터 변경시 트랜잭션을 시작해야 한다.
        transaction.begin(); // [트랜잭션] 시작
        Member memberA = new Member(1L, "memberA");
        Member memberB = new Member(2L, "memberB");
        em.persist(memberA);
        em.persist(memberB);
        //여기까지 INSERT SQL을 데이터베이스에 보내지 않는다.

        //커밋하는 순간 데이터베이스에 INSERT SQL을 보낸다.
        transaction.commit(); // [트랜잭션] 커밋

        em.close();
        emf.close();
    }
}
