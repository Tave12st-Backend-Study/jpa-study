package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Team teamA = new Team();
            teamA.setName("teamA");

            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");

            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);

            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);

            em.persist(member3);

            em.flush();
            em.clear();

            // 엔티티 직접 사용
            String query = "select m From Member m where m = :member";
            Member findMember = em.createQuery(query, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();
            System.out.println("findMember = " + findMember);

            String query2 = "select m From Member m where m.team = :team";

            // 외래 키 값으로 사용
            List<Member> resultList = em.createQuery(query2, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();
            for (Member member : resultList) {
                System.out.println("member = " + member);
            }

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("엔티티를 파라미터로 전달하거나 식별자를 직접 전달하거나 똑같이, 실행된 쿼리는");
            System.out.println("select m.* from Member m where m.id=?");
            System.out.println("위와 같다. 실제 쿼리는 아래와 같다.");
            System.out.println("        select\n" +
                    "            member0_.id as id1_0_,\n" +
                    "            member0_.age as age2_0_,\n" +
                    "            member0_.memberType as memberTy3_0_,\n" +
                    "            member0_.TEAM_ID as TEAM_ID5_0_,\n" +
                    "            member0_.username as username4_0_ \n" +
                    "        from\n" +
                    "            Member member0_ \n" +
                    "        where\n" +
                    "            member0_.id=?");

            System.out.println("-------------------------------------------------------------------------------------");

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
