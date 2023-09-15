package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args) {
        //엔티티 매니저 팩토리 생성
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //영속상태가 아닌 단순히 객체에 데이터 주입
        Member member = new Member();
        member.setName("member1");
        member.setName("회원1");
        //------ 이때까진 비영속 상태 ------

        //엔티티 매니저 생성
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        //객체를 저장한 상태 -> 영속상태
        em.persist(member);

    }
}
