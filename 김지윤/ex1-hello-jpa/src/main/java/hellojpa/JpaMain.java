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
            Member member = new Member();
            member.setName("hello");

            em.persist(member);

            em.flush();
            em.clear();

//            Member findMember = em.find(Member.class, member.getId()); // 쿼리문이 실행된다.
            Member findMember = em.getReference(Member.class, member.getId()); // 쿼리문이 실행되지 않는다.
            System.out.println("findMember.id = " + findMember.getId()); // 시퀀스를 통해 값을 엔티티에 저장했으므로 쿼리문이 실행되지 않는다.
            System.out.println("findMember.username = " + findMember.getName()); // 프록시 객체가 초기화되면서 쿼리문이 실행된다.

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

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
