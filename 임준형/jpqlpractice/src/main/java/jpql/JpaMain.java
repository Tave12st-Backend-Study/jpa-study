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

            em.flush();
            em.clear();
            
            // 제너릭으로 같고 있음
            List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            Member findMember = resultList.get(0);
            findMember.setAge(20);

            System.out.println("findMember.getAge() == 20? " + (findMember.getAge() == 20)); // true
            System.out.println("true라면, 엔티티 프로젝션시 영속성 컨텍스트에서 관리되는 것이다.");   // 영속성 컨텍스트에서 관리 중

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
