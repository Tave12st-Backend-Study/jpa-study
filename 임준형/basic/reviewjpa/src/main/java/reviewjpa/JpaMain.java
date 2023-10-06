package reviewjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();
        // code 시작

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            List<Member> memberList = em.createQuery("select m From Member m where m.username like '%kim%'",
                    Member.class).getResultList();

            System.out.println("================ 실제 쿼리 ================\n" +
                    "    select\n" +
                    "        m \n" +
                    "    From\n" +
                    "        Member m \n" +
                    "    where\n" +
                    "        m.username like '%kim%' */ select\n" +
                    "            member0_.MEMBER_ID as MEMBER_I1_4_,\n" +
                    "            member0_.city as city2_4_,\n" +
                    "            member0_.street as street3_4_,\n" +
                    "            member0_.zipcode as zipcode4_4_,\n" +
                    "            member0_.TEAM_ID as TEAM_ID6_4_,\n" +
                    "            member0_.USERNAME as USERNAME5_4_ \n" +
                    "        from\n" +
                    "            Member member0_ \n" +
                    "        where\n" +
                    "            member0_.USERNAME like '%kim%'\n" +
                    "================ 실제 쿼리 ================");

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
