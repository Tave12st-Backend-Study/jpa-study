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

        CriteriaBuilder cb = em.getCriteriaBuilder();
        try {

            Member member = new Member();
            member.setUsername("AA");
            em.persist(member);

            System.out.println("================ 자동 flush 가 됨 왜? JPQL 이니까");
            
            System.out.println("================ 쿼리 나가는 시점 ================\n");

            List<Member> resultList = em.createNativeQuery("select MEMBER_ID, city, street, zipcode, USERNAME from Member", Member.class)
                    .getResultList();

            System.out.println("================ 쿼리 나가는 시점 ================\n");

            System.out.println("================ 실제 쿼리 ================\n" +
            "/* dynamic native SQL query */ \n" +
                    "    select\n" +
                    "        MEMBER_ID,\n" +
                    "        city,\n" +
                    "        street,\n" +
                    "        zipcode,\n" +
                    "        USERNAME \n" +
                    "    from\n" +
                    "        Member"
            + "\n================ 실제 쿼리 ================");

            for (Member m : resultList) {
                System.out.println("m.getUsername() = " + m.getUsername()); // m.getUsername() = AA
            }
            
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
