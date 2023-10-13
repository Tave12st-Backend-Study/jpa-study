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

            System.out.println("---------- 컬렉션에서 fetch 조인 ----------");
            String collectionFetchJoinQuery = "select distinct t From Team t join fetch t.memberList";
        
            List<Team> result3 = em.createQuery(collectionFetchJoinQuery, Team.class).getResultList();
            for (Team team : result3) {
                System.out.println("team.getName() = " + team.getName());
                System.out.println("team.getMemberList().size() = " + team.getMemberList().size());

                for (Member member : team.getMemberList()) {
                    System.out.println("member = " + member);
                }
                System.out.println("------------------------------------");
            }

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("방금 문제를 해결하려면 DISTINCT를 활용하면된다.");
            System.out.println("SQL의 DISTINCT는 중복된 결과를 제거하는 명령이다.");
            System.out.println("JPQL의 DISTINCT는 2가지 기능을 제공한다.");
            System.out.println("1. SQL에 DISTINCT를 추가한다.");
            System.out.println("2. 애플리케이션에서 엔티티의 중복을 제거한다.");
            System.out.println("JPQL에서 같은 식별자를 가진 Team 엔티티를 제거하게된다.");
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
