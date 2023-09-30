package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static java.time.LocalDateTime.now;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member1 = new Member();
            member1.setName("member1");
            em.persist(member1);

//            Member member2 = new Member();
//            member2.setName("member2");
//            em.persist(member2);

            em.flush();
            em.clear();

            Member m1 = em.find(Member.class, member1.getId());
            System.out.println("m1 = "+m1.getClass());
            Member reference = em.getReference(Member.class, member1.getId());
            System.out.println("reference = "+reference.getClass());

//            Member m2 = em.getReference(Member.class, member2.getId());
            System.out.println("m1 = reference : "+(m1.getClass()==reference.getClass()));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
    private static void logic(Member m1,Member m2){
        System.out.println("m1 == m2 : "+(m1 instanceof Member));
        System.out.println("m1 == m2 : "+(m2 instanceof Member));
    }
}


