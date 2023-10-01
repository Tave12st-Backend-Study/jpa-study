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

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());
            Member m2 = em.find(Member.class, member1.getId());
            Member m3 = em.getReference(Member.class, member2.getId());

            System.out.println("_____________________");
            System.out.println("en.find, em.find, \n" +
                    " m1.getClass() == m2.getClass() "  + (m1.getClass() == m2.getClass()));   // true

            System.out.println("_____________________");
            System.out.println("en.find, em.Reference, \n" +
                    " m2.getClass() == m3.getClass() " + (m2.getClass() == m3.getClass()));   // false

            System.out.println("_____________________");
            System.out.println("en.find, em.Reference, \n" +
                    " m1 instanceOf Member " + (m1 instanceof Member));   // false
            System.out.println("_____________________");

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
