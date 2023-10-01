package reviewjpa;

import org.hibernate.Hibernate;
import reviewjpa.superclass.Item;
import reviewjpa.superclass.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();
        // code 시작

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamB);
            em.persist(member2);


            em.flush();
            em.clear();

            System.out.println("----- 만약 member와 team이 90프로 이상 같이 사용한다면 EAGER를 사용할것 -----");
            System.out.println("----- 프록시 객체가 존재하지 않기 때문에 초기화 과정 필요 없음 -----");
            System.out.println("----- 한번에 쿼리가 나감 -----");
            System.out.println("----- 하지만 실무에서는 가급적 지연 로딩만 사용한다. -----");

            System.out.println("----- em.find는 pk를 찍어서 가져오기 때문에 JPA가 내부적으로 최적화 가능하다.");
            Member member = em.find(Member.class, member1.getId());

            System.out.println("----- JPQL은 우선 SQL로 번역이된다. ----- ");
            System.out.println("----- Member를 전체 조회하는 쿼리인데, 즉시로딩이므로 Team의 값이 다 들어가있어야한다. -----");
            List<Member> selectMFromMemberM =
                    em.createQuery("select m from Member m", Member.class).getResultList();
            System.out.println("----- SQL: select * " +
                                            "from Member" +
                                            " -----");
            System.out.println("----- 이렇게 가져온 후, Member가 즉시로딩이므로 Team 조회하는 쿼리 출력 -----");
            System.out.println("----- SQL: select * " +
                                            "from Team " +
                                           "Where TEAM_ID = MEMBER_ID -----");

            System.out.println("----- team이 지금 2개인데, member를 전체조회하는 쿼리 1개와, " +
                    "각 team마다 조회하는 쿼리 n개가 나오기 때문에 n+1 문제라고한다. -----");

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
