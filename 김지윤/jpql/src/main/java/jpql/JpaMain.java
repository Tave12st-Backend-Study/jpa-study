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
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            // 페치 조인을 사용하지 않는 경우
            String query1 = "select m from Member m ";

            List<Member> result1 = em.createQuery(query1, Member.class)
                    .getResultList();

            for (Member member : result1) {
                System.out.println("userName = " + member.getUsername() + ", " +
                        "teamName = " + member.getTeam().getName());
                // fetch = FetchType.LAZY로 설정했으므로 team은 프록시 객체로 가져온다.
                // 회원1, 팀A(SQL)
                // 회원2, 팀A(1차 캐시)
                // 회원3, 팀B(SQL)
                // ...
                // 회원 100명 -> N + 1
            }

            em.flush();
            em.clear();

            // 페치 조인을 사용하는 경우
            String query2 = "select m from Member m join fetch m.team";

            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();

            for (Member member : result2) {
                System.out.println("userName = " + member.getUsername() + ", " +
                        "teamName = " + member.getTeam().getName());
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
