package reviewjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {

            CriteriaQuery<Member> query = cb.createQuery(Member.class);
            Root<Member> from = query.from(Member.class);

            CriteriaQuery<Member> where = query.select(from).where(cb.equal(from.get("username"), "kim"));
            System.out.println("================ 쿼리 나가는 시점 ================\n");
            List<Member> memberList = em.createQuery(where).getResultList();
            System.out.println("================ 쿼리 나가는 시점 ================\n");
            System.out.println("================ 실제 쿼리 ================\n" +
                    "    select\n" +
                    "        generatedAlias0 \n" +
                    "    from\n" +
                    "        Member as generatedAlias0 \n" +
                    "    where\n" +
                    "        generatedAlias0.username=:param0 */ select\n" +
                    "            member0_.MEMBER_ID as MEMBER_I1_4_,\n" +
                    "            member0_.city as city2_4_,\n" +
                    "            member0_.street as street3_4_,\n" +
                    "            member0_.zipcode as zipcode4_4_,\n" +
                    "            member0_.TEAM_ID as TEAM_ID6_4_,\n" +
                    "            member0_.USERNAME as USERNAME5_4_ \n" +
                    "        from\n" +
                    "            Member member0_ \n" +
                    "        where\n" +
                    "            member0_.USERNAME=?"
            + "\n================ 실제 쿼리 ================");

            tx.commit(); // 성공하면 커밋

        } catch (Exception e) {
            tx.rollback(); // 실패하면 롤백
            e.printStackTrace();
        } finally {
            em.close();
        }

        emFactory.close();
        // code 끝
    }

}
