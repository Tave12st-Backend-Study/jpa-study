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
            TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);

            List<Member> resultList = query.getResultList();

            System.out.println("----- 결과가 없으면 빈 리스트 반환 -----");
            for (Member m : resultList) {
                System.out.println("m.getUsername() = " + m.getUsername());
            }

            System.out.println("----- 결과가 없으면 예외가 발생하는 것이 못마땅, SpringDataJpa에선 이를 Null로 반환 -----");
            System.out.println("----- 결과가 없으면 NoResultException 반환 -----");
            System.out.println("----- 결과가 둘 이상이면 NonUniqueResultException 반환 -----");
            Member singleMember = query.getSingleResult();
            System.out.println("singleMember.getUsername() = " + singleMember.getUsername());


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
