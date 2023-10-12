package jpql;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("관리자1");
            member1.setTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setTeam(team);
            em.persist(member2);

            em.flush();
            em.clear();

            // 상태 필드 경로 탐색
            String query1 = "select m.username from Member m";
            List<String> result1 = em.createQuery(query1, String.class)
                    .getResultList();

            for (String s : result1) {
                System.out.println("s = " + s);
            }

            // 단일 값 연관 경로
            String query2 = "select m.team from Member m";
            List<Team> result2 = em.createQuery(query2, Team.class)
                    .getResultList();

            for (Team s : result2) {
                System.out.println("s = " + s);
            }

            // 컬렉션 값 연관 경로 - 묵시적 조인
            String query3 = "select t.members from Team t"; // 묵시적 조인
            List<Collection> result3 = em.createQuery(query3, Collection.class)
                            .getResultList();

            System.out.println("result = " + result3);

            // 컬렉션 값 연관 경로 - 묵시적 조인
            String query4 = "select m from Team t join t.members m"; // 명시적 조인
            List<Member> result4 = em.createQuery(query4, Member.class)
                    .getResultList();

            for (Member m : result4) {
                System.out.println("m = " + m);
            }


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }
}
