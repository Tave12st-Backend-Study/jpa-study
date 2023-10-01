package reviewjpa;

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


            // 쿼리를 가져올 때 한 번에 다 가져오는 것이 이상적
            Member member = em.find(Member.class, 1L);
            printMember(member);
//            printMemberAndTeam(member);


            tx.commit(); // 성공하면 커밋

        } catch (Exception e) {
            tx.rollback(); // 실패하면 롤백
        } finally {
            em.close();
        }

        emFactory.close();
        // code 끝
    }

    // member만 조회시 연관관계 매핑이 걸려있다고해서 Team까지 갖고오게 될 경우 손해
    private static void printMember(Member member) {
        System.out.println("member = " + member);
    }

    // member와 team 둘 다 조회하므로 한 번에 가져오는 것이 이득
    private static void printMemberAndTeam(Member member) {
        String username = member.getUsername();
        System.out.println("username = " + username);

        Team team = member.getTeam();
        System.out.println("team = " + team.getName());
    }
}
