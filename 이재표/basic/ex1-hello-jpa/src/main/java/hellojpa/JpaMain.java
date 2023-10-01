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
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setName("member1");
            member1.setTeam(team);

            em.persist(member1);

            em.flush();
            em.clear();

            Member member = em.find(Member.class, member1.getId());
            System.out.println("m = " + member.getTeam().getClass());

            System.out.println("=======");
            member.getTeam().getName();
            System.out.println("=======");
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
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


