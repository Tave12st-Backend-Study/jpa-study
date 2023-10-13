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
            String collectionFetchJoinQuery = "select t From Team t join fetch t.memberList";
        
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
            System.out.println("teamA는 회원이 2명이다.");
            System.out.println("teamB는 회원이 1명이다.");
            System.out.println("그런데 왜");
            System.out.println("'team.getName() = teamA\n" +
                    "team.getMemberList().size() = 2'");
            System.out.println("이렇게 2번 출력될까? ");
            System.out.println("DB입장에서 1:N 조회하면 데이터가 뻥튀기가 된다.");
            System.out.println("TeamA 입장에서는 1개인데, TeamA에 속한 Member가 2명이므로 2줄이 되는 것이다.");
            System.out.println("TeamA의 대한 정보를 DB에서 처음에 갖고오고, 같은 TeamA는 이제 1차 캐시에서 갖고온다.");
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
