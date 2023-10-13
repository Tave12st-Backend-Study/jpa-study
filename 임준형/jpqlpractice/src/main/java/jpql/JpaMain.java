package jpql;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

public class JpaMain {
    public static void main(String[] args) {

        EntityManagerFactory emFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emFactory.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin(); // transaction 시작

        try {

            Team team = new Team();
            team.setName("teamA");

            em.persist(team);

            Member member = new Member();
//            member.setUsername("member");
            member.setAge(10);
            member.setTeam(team);
            member.setMemberType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("----- COALESCE: 하나씩 조회해서 null이 아니면 반환 -----");

            System.out.println("----- 쿼리 나가는 시점 -----");

            String query = "select coalesce(m.username, '이름 없는 회원') " +
                    "from Member m ";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();

            System.out.println("----- 쿼리 끝나는 시점-----\n");

            for (String s : resultList) {
                System.out.println("s = " + s);
            }

            System.out.println("\n----- 실제 쿼리 -----");
            System.out.println("        select\n" +
                    "            coalesce(member0_.username,\n" +
                    "            '이름 없는 회원') as col_0_0_ \n" +
                    "        from\n" +
                    "            Member member0_");
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
