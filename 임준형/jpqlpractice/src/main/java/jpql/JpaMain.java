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
            member.setUsername("userNNAAMMEE");
            member.setAge(10);
            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("----- 쿼리 나가는 시점 -----");

            // username과 age는 타입이 다르므로 Member.class로 타입을 정할 수 없음
            List<MemberDto> resultList = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                    .getResultList();

            MemberDto memberDto = resultList.get(0);
            System.out.println("----- 쿼리 끝나는 시점-----\n");

            System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
            System.out.println("memberDto.getAge() = " + memberDto.getAge());

            System.out.println("\n----- 실제 쿼리 -----");
            System.out.println("    select\n" +
                    "        new jpql.MemberDto(m.username,\n" +
                    "        m.age) \n" +
                    "    from\n" +
                    "        Member m");
            System.out.println("----- 실제 쿼리 끝 -----");

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
