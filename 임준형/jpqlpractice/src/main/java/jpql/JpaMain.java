package jpql;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Team team = new Team();
            team.setName("teamA");

            em.persist(team);

            Member member = new Member();
            member.setUsername("member");
            member.setAge(10);
            member.setTeam(team);
            member.setMemberType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("----- 쿼리 나가는 시점 -----");
            String query = "select m from Member m " +
                            "where m.memberType = jpql.MemberType.ADMIN";

            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member1 : resultList) {
                System.out.println(member1);
            }
            System.out.println("----- 쿼리 끝나는 시점-----\n");

            System.out.println("\n----- 실제 쿼리 -----");
            System.out.println("        select\n" +
                    "            member0_.id as id1_0_,\n" +
                    "            member0_.age as age2_0_,\n" +
                    "            member0_.memberType as memberTy3_0_,\n" +
                    "            member0_.TEAM_ID as TEAM_ID5_0_,\n" +
                    "            member0_.username as username4_0_ \n" +
                    "        from\n" +
                    "            Member member0_ \n" +
                    "        where\n" +
                    "            member0_.memberType='ADMIN'");
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
