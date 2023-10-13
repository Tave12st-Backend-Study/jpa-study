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

            System.out.println("----- 쿼리 나가는 시점 -----");
            System.out.println("----- 서브쿼리를 JPA에서는 select 안에 불가능하지만, 하이버네이트는 가능함. -----");
            System.out.println("----- 당연히 where 에도 가능하지만 From 절에는 불가능하다 !  -----");
            String query = "select (select avg(m1.age) From Member m1) from Member m left join m.team t on t.name='teamA'";

            Double singleResult = (Double) em.createQuery(query).getSingleResult();
            System.out.println("singleResult = " + singleResult);

            System.out.println("----- 쿼리 끝나는 시점-----\n");

            System.out.println("\n----- 실제 쿼리 -----");
            System.out.println("        select\n" +
                    "                (select\n" +
                    "                    avg(cast(member2_.age as double)) \n" +
                    "            from\n" +
                    "                Member member2_) as col_0_0_ \n" +
                    "        from\n" +
                    "            Member member0_ \n" +
                    "        left outer join\n" +
                    "            Team team1_ \n" +
                    "                on member0_.TEAM_ID=team1_.id \n" +
                    "                and (\n" +
                    "                    team1_.name='teamA'\n" +
                    "                )");
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
