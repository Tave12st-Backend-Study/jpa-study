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

            String query = "select t From Team t";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("resultList.size() = " + resultList.size());

            for (Team team : resultList) {
                System.out.println("team.getName() = " + team.getName());
                System.out.println("team.getMemberList().size() = " + team.getMemberList().size());

                for (Member member : team.getMemberList()) {
                    System.out.println("member = " + member);
                }
            }

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("-- fetch join에서는 별칭(앨리어스)를 사용하면 안된다.--");
            System.out.println("fetch join은 본인과 연관된 모두를 갖고 온다.");
            System.out.println("앨리어스와 where를 통해 데이터를 조작하면, 본인과 연관된 모두가 아닌 특정 일부일 수 있기 때문에 좋지 않다.");

            System.out.println("\n-- 둘 이상의 컬렉션은 fetch join을 사용할 수 없다.");

            System.out.println("\n-- 컬렉션은 fetch join하면 페이징 API(setFirstResult, setMaxResults)를 사용할 수 없다 --");
            System.out.println("일대일, 다대다 같은 단일 값 연관 필드들은 fetch join해도 페이징이 가능하다.");
            System.out.println("하지만 일대다는 데이터가 뻥튀기가 되기 떄문에, 갖고 오다가 짤려 데이터가 누락됐지만 데이터가 적게 표출되는 것이 정상처럼 보임");
            System.out.println("\n컬렉션일 때의 이를 해결하기 위한 방법으로는, @BatchSize 를 사용하면 해결할 수 있다.");
            System.out.println("지금 @BatchSize를 100으로 했기 때문에, Lazy이지만 100개씩 조회를 한다.");
            System.out.println("직접 @BatchSize를 사용하거나, default 설정으로 세팅을 한다.");
            System.out.println("여러 테이블을 조인해서 엔티티가 가진 모양이 아닌 전혀 다른 결과를 내야하면, ");
            System.out.println("페치 조인보다는 일반 조인을 사용하고 필요한 데이터들만 조회해서 DTO로 반환하는 것이 효과적이다.");
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
