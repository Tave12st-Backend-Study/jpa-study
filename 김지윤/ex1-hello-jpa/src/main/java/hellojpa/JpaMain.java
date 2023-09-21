package hellojpa;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 팀 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team); // 영속 상태

            // 회원 저장
            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            em.persist(member); // 영속 상태

            // 회원이 속한 팀 조회
            Member findMember = em.find(Member.class, member.getId()); // 1차 캐시에서 member를 가져온다.
            Team findTeam = findMember.getTeam();
            System.out.println("findTeam = " + findTeam.getName());

            // 회원이 속한 팀 수정
            Team teamB = new Team(); // 새로운 팀
            teamB.setName("TeamB");
            em.persist(teamB);
            findMember.setTeam(teamB);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();

    }
}
