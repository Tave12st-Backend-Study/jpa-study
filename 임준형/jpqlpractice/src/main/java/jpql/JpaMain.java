package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Member member = new Member();
            member.setUsername("newnew");
            member.setAge(10);
            em.persist(member);

            // 제너릭으로 같고 있음
            TypedQuery<Member> query = em.createQuery("select m from Member m where m.username = :username", Member.class);
            Member singleResult = query.setParameter("username", "newnew")
                    .getSingleResult();
            System.out.println("----- 쿼리가 나가는 시점 딩-----");
            System.out.println("singleResult.getUsername() = " + singleResult.getUsername());
            System.out.println("----- 쿼리가 종료 -----");
            System.out.println("----- 실제 나가는 쿼리\n" +
                    "Hibernate: \n" +
                    "    /* select\n" +
                    "        m \n" +
                    "    from\n" +
                    "        Member m \n" +
                    "    where\n" +
                    "        m.username = :username" +
            "\n -----");

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
