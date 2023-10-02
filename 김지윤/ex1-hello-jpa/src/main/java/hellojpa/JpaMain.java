package hellojpa;

import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.List;

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

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setName("member1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("member2");
            member2.setTeam(teamB);
            em.persist(member2);

            em.flush();
            em.clear();

            List<Member> members = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

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
