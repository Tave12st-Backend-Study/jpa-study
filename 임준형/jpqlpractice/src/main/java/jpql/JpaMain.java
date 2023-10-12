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
            member.setUsername("newnew");
            member.setAge(10);
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();
            
            // 사용 xx 후에 경로 표현식에서 자세히 다룸
            List<Team> resultList3 = em.createQuery("select m.team from Member m", Team.class)
                    .getResultList();

            // 위는 예측이 불가능한 쿼리, 아래로 사용할 것
            List<Team> resultList = em.createQuery("select m.team from Member m join m.team", Team.class)
                    .getResultList();



            Team findTeam = resultList.get(0);
            // join query가 나감
            System.out.println("----- 쿼리 나가는 시점 -----");
            System.out.println("findTeam.getName() = " + findTeam.getName());
            System.out.println("----- 쿼리 끝나는 시점 -----");

            System.out.println("----- 실제 쿼리 -----");
            System.out.println("select\n" +
                    "            team1_.id as id1_3_,\n" +
                    "            team1_.name as name2_3_ \n" +
                    "        from\n" +
                    "            Member member0_ \n" +
                    "        inner join\n" +
                    "            Team team1_ \n" +
                    "                on member0_.TEAM_ID=team1_.id");
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
