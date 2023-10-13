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

            List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                    .setParameter("username", "회원1")
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member);
            }

            System.out.println("-------------------------------------------------------------------------------------");
            System.out.println("Named Query 사용법");
            System.out.println("엔티티에 미리 생성해 놓는다. 나는 아래와 같이 미리 생성해두었다.");
            System.out.println("@NamedQuery(\n" +
                    "        name = \"Member.findByUsername\",\n" +
                    "        query = \"select m from Member m where m.username = :username\"\n" +
                    ")");
            System.out.println("애플리케이션이 로딩될 시점에 쿼리를 검증한다. 이것이 매우 유용하다 !! 하지만.. ");
            System.out.println("compile 오류가 발생함");
            System.out.println("Spring Data Jpa에서 사용하는 @Query는 NamedQuery로 구현한 것이다. 그것을 사용하고 엔티티에 직접사용은 별로다!");
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
