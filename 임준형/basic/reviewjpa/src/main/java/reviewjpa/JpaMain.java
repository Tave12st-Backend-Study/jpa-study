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

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(team);
            em.persist(member1);

            em.flush();
            em.clear();

            System.out.println("----- 만약 member와 team이 90프로 이상 같이 사용한다면 EAGER를 사용할것 -----");
            System.out.println("----- 프록시 객체가 존재하지 않기 때문에 초기화 과정 필요 없음 -----");
            System.out.println("----- 한번에 쿼리가 나감 -----");
            System.out.println("----- 하지만 실무에서는 가급적 지연 로딩만 사용한다. -----");


            Member member = em.find(Member.class, member1.getId());
            System.out.println(" flag1 ----- ----- ");
            Team getTeam = member.getTeam();    // proxy를 가져오기 때문에 어떠한 쿼리도 나가지 않음
            System.out.println(" flag2 ----- ----- ");
            System.out.println("getTeam.getClass() = " + getTeam.getClass());   // proxy

            System.out.println(" flag3 ----- ----- ");
            System.out.println("getTeam.getName() = " + getTeam.getName());


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
