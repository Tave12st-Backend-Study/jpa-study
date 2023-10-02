package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;

import static java.time.LocalDateTime.now;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Address address = new Address("city", "street", "10000");
            Member member1 = new Member();
            member1.setName("hello");
            member1.setHomeAddress(address);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
            member2.setHomeAddress(address);
            em.persist(member2);

            member1.getHomeAddress().setCity("newCity");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(Member m1, Member m2) {
        System.out.println("m1 == m2 : " + (m1 instanceof Member));
        System.out.println("m1 == m2 : " + (m2 instanceof Member));
    }
}


