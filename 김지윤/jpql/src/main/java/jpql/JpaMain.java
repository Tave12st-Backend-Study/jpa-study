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
            Member member1 = new Member();
            member1.setUsername("관리자1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            em.persist(member2);

            em.flush();
            em.clear();

            // JPQL 기본 함수 - concat
            String query1 = "select concat('a', 'b') from Member m";
            // JPQL 기본 함수 - substring
            String query2 = "select substring(m.username, 2, 3) from Member m";
            // JPQL 기본 함수 - locate
            String query3 = "select locate('de', 'abcdefg') from Member m";
            // JPQL 기본 함수 - size
            String query4 = "select size(t.members) from Team t";
            // 사용자 정의 함수 - group_concat
            String query5 = "select function('group_concat', m.username) from Member m";
            String query6 = "select group_concat(m.username) from Member m";

            List<String> result = em.createQuery(query6, String.class)
                    .getResultList();

            for (String s : result) {
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
