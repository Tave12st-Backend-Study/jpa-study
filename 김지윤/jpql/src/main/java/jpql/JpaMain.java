package jpql;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 내부 조인
            String query1 = "select m from Member m inner join m.team t";
            List<Member> result1 = em.createQuery(query1, Member.class)
                    .getResultList();

            // 외부 조인
            String query2 = "select m from Member m left outer join m.team t";
            List<Member> result2 = em.createQuery(query2, Member.class)
                    .getResultList();

            // 세타 조인
            String query3 = "select m from Member m, Team t where m.username = t.name";
            List<Member> result3 = em.createQuery(query3, Member.class)
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
}
