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

            System.out.println("----- 회원과 팀을 조인하면서, 팀 이름이 A인 팀만 조인 -----");
            System.out.println("----- 쿼리 나가는 시점 -----");
            // outer 생략 가능, 조인 대상 필터링
            String query = "select m from Member m left join m.team t on t.name='teamA'";
            List<Member> result = em.createQuery(query, Member.class)
                    .getResultList();

            System.out.println("----- 쿼리 끝나는 시점-----\n");

            System.out.println("\n----- 실제 쿼리 -----");
            System.out.println("    select\n" +
                    "        m \n" +
                    "    from\n" +
                    "        Member m \n" +
                    "    left join\n" +
                    "        m.team t \n" +
                    "            on t.name='teamA' */ select\n" +
                    "                member0_.id as id1_0_,\n" +
                    "                member0_.age as age2_0_,\n" +
                    "                member0_.TEAM_ID as TEAM_ID4_0_,\n" +
                    "                member0_.username as username3_0_ \n" +
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
