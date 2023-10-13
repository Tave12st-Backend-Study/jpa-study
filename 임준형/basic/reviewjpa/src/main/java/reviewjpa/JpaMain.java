package reviewjpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Member member = new Member();
            member.setUsername("AA");
            em.persist(member);

            System.out.println("JPA와 아무 관련이 없는 SQL은 JPQL이 아니기 대문에, flsuh가 안되므로 flush를 직접 해줘야한다.");
            // dbconnect.excuteQuery("select * from member')

            em.flush();

//            for (Member m : resultList) {
//                System.out.println("m.getUsername() = " + m.getUsername()); // m.getUsername() = AA
//            }
            
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
