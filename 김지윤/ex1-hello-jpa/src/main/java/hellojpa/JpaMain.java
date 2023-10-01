package hellojpa;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member1.getId()); // 엔티티 객체
            System.out.println("findMember = " + findMember.getClass());

            Member refMember = em.getReference(Member.class, member1.getId()); // 엔티티 객체
            System.out.println("refMember = " + refMember.getClass());

            System.out.println("findMember == refMember: " + (findMember == refMember)); // True

            em.flush();
            em.clear();

            System.out.println("-------------------------");

            Member refMember2 = em.getReference(Member.class, member1.getId()); // 프록시 객체
            System.out.println("refMember2 = " + refMember2.getClass());

            Member refMember3 = em.getReference(Member.class, member1.getId()); // 프록시 객체
            System.out.println("refMember3 = " + refMember3.getClass());

            System.out.println("refMember2 == refMember3: " + (refMember2 == refMember3)); // True

            em.flush();
            em.clear();

            System.out.println("-------------------------");

            Member refMember4 = em.getReference(Member.class, member1.getId()); // 프록시 객체
            System.out.println("refMember4 = " + refMember4.getClass());

            Member findMember2 = em.find(Member.class, member1.getId()); // 프록시 객체
            System.out.println("findMember2 = " + findMember2.getClass());

            System.out.println("refMember3 == findMember2: " + (refMember4 == findMember2)); // True

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void logic(Member m1, Member m2) {
        System.out.println("m1 == m2: " + (m1 instanceof Member));
        System.out.println("m1 == m2: " + (m2 instanceof Member));
    }

    private static void printMember(Member member) {
        System.out.println("member = " + member.getName());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getName();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("Team = " + team.getName());
    }
}
