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
            member.setUsername("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

            em.flush();
            em.clear();

            // 기본 CASE
            String query1 =
                    "select " +
                            "case when m.age <= 10 then '학생요금' " +
                            "     when m.age >= 60 then '경로요금' " +
                            "     else '일반요금' " +
                            "end " +
                    "from Member m";
            List<String> resultList1 = em.createQuery(query1, String.class)
                    .getResultList();

            for (String s : resultList1) {
                System.out.println("s = " + s);
            }

            // COALESCE
            String query2 = "select coalesce(m.username, '이름 없는 회원') as username " +
                    "from Member m";
            List<String> resultList2 = em.createQuery(query2, String.class)
                    .getResultList();

            for (String s : resultList2) {
                System.out.println("s = " + s);
            }

            // NULLIF
            String query3 = "select nullif(m.username, '관리자') from Member m";
            List<String> resultList3 = em.createQuery(query3, String.class)
                    .getResultList();

            for (String s : resultList3) {
                System.out.println("s = " + s);
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
