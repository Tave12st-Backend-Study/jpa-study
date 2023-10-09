package jpql;

import javax.persistence.*;

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
            TypedQuery<Member> selectMFromMemberM = em.createQuery("select m from Member m", Member.class);
            System.out.println("----- 반환 타입이 string이면 아래와 같이 가능 -----");
            TypedQuery<String> stringQuery = em.createQuery("select m.username from Member m", String.class);

            System.out.println("----- Type 정보가 string과 int로 복합적이므로, 이 때 (반환 타입이 명확하지 않을 때)는 Query로 해야함. -----");
            Query objectQuery = em.createQuery("select m.username, m.age from Member m", String.class);

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
