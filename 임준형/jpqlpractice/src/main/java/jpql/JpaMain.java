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

            String query = "select m From Member m";

            List<Member> result1 = em.createQuery(query, Member.class).getResultList();

            for (Member findMember : result1) {
                System.out.println("findMember.getUsername() + findMember.getTeam().getName() = "
                        + findMember.getUsername() + " " + findMember.getTeam().getName());
            }

            em.flush();
            em.clear();

            System.out.println("---------- 위 N+1 문제를 해결 ----------");

            String fetchJoinQuery = "select m From Member m join fetch m.team";

            List<Member> result3 = em.createQuery(fetchJoinQuery, Member.class).getResultList();
            for (Member findFetchJoinMember : result3) {
                System.out.println("findFetchJoinMember.getUsername = " + findFetchJoinMember.getUsername() + " " + findFetchJoinMember.getTeam().getName()) ;
            }

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("지연로딩이므로 Team은 현재 Proxy객체, member.getUsername()했을 때는 조회되지 않음");
            System.out.println("member.getTeam().getName() -> 이 때 Team에 대한 정보 조회 쿼리가 나감");
            System.out.println("지금 영속성 컨텍스트가 깔끔한 상태이기 때문에 회원1일 때 팀 A를 DB에서 가져옴 SQL");
            System.out.println("회원 2도 팀 A인데, 영속성 컨텍스트 속 1차캐시에 존재하기 때문에 1차캐시에서 가져옴");
            System.out.println("회원 3은 팀 B이고, 영속성 컨텍스트에 없기 때문에 DB에서 가져옴 SQL");
            System.out.println("회원 100명을 조회시 팀이 전부 다르다면, 조회하기 위해 101 번이 돈다. 그래서 N+1 문제라고 함");
            System.out.println("즉시로딩을 사용하면 모든 엔티티를 조회하므로 안좋고, 지연로딩시 N+1 문제가 발생하므로 안 좋음");
            System.out.println("이를 관련된 쿼리만을 조인하여 한 번에 조인하는 fetch join으로 해결한다. 이 때, Proxy가 아닌 엔티티객체이다.");
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
