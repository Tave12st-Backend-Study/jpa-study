package reviewjpa;

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

            Member member = new Member();
            member.setUsername("Embedded type");

            Address build = Address.builder()
                    .city("city")
                    .street("street")
                    .zipcode("10000")
                    .build();
            member.setHomeAddress(build);

            Period period = Period.builder()
                    .startDate(LocalDateTime.now())
                    .endDate(LocalDateTime.MAX)
                    .build();

            member.setWorkPeriod(period);

            em.persist(member);

            em.flush();
            em.clear();

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
