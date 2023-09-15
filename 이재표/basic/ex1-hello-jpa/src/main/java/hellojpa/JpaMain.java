package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //영속상태가 아닌 단순히 객체에 데이터 주입
        Member member = new Member();
        member.setId(1L);
        member.setName("회원1");
        //------ 이때까진 비영속 상태 ------

        //엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        //객체를 저장한 상태 -> 영속상태
        em.persist(member);
        // em.find(원하는 객체,pk값) : 조회

        // em.remove(조회한 객체) : 삭제

        /*조회한 객체의 데이터만을 수정하면 트랜잭션을 커밋하는 시점에 수정한다.
        Member findMember = em.find(Member.class, 1L);
        findMember.setName("helloB");*/

        //트랜잭션 커밋
        tx.commit();

        em.close();
        emf.close();
    }
}
