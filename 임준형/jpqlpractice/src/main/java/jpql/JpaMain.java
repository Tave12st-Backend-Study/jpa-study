package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            for (int i = 0; i < 100; i++) {
                Member member = new Member();
                member.setUsername("member" + i);
                member.setAge(i);
                em.persist(member);

            }

            em.flush();
            em.clear();

            System.out.println("----- 쿼리 나가는 시점 -----");

            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(10)
                    .setMaxResults(20)
                    .getResultList();

            System.out.println("----- 쿼리 끝나는 시점-----\n");

            System.out.println("resultList.size() = " + resultList.size());
            for (Member mm : resultList) {
                System.out.println("mm = " + mm);
            }
            System.out.println("\n----- 실제 쿼리 -----");
            System.out.println("        select\n" +
                    "            member0_.id as id1_0_,\n" +
                    "            member0_.age as age2_0_,\n" +
                    "            member0_.TEAM_ID as TEAM_ID4_0_,\n" +
                    "            member0_.username as username3_0_ \n" +
                    "        from\n" +
                    "            Member member0_ \n" +
                    "        order by\n" +
                    "            member0_.age desc limit ? offset ?");
            System.out.println("----- 실제 쿼리 끝 -----");

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
