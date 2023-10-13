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
            member.setUsername("관리자");
            member.setAge(10);
            member.setTeam(team);
            member.setMemberType(MemberType.ADMIN);

            em.persist(member);

            em.flush();
            em.clear();


            System.out.println("----- 쿼리 시작 시점-----\n");

            String concatQuery = "select concat('a', 'b') " +
                    "from Member m "; // ab 연결

            String substringQuery = "select substring(m.username, 2, 3) " +
                    "from Member m "; // 2번째글자 반환

            String locateQuery = "select locate('de', 'abcdefg') " +
                    "from Member m ";   // 4반환

            String collectionSize = "select size(t.memberList) " +
                    "From Team t";      // memberList 크기 반환

            System.out.println("----- 쿼리 끝나는 시점-----\n");

            System.out.println("\n----- 실제 쿼리 -----");
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
