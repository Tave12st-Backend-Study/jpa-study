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

        //엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        //JPQL을 통한 쿼리 작성
        List<Member> resultList = em.createQuery("select m from Member as m", Member.class).getResultList();
        System.out.println("결과크기 : "+resultList.size());

        tx.commit();

        em.close();
        emf.close();
    }
}
