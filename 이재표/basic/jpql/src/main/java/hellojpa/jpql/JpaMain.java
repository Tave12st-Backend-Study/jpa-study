package hellojpa.jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("teamA");
            member.setAge(10);
member.setMemberType(MemberType.ADMIN);
            member.setTeam(team);
            em.persist(member);


            em.flush();
            em.clear();

            String query = "select m.username,'HELLO', TRUE FROM Member m where m.memberType=hellojpa.jpql.MemberType.USER";
            List<Object[]> result = em.createQuery(query)
                    .getResultList();
            for (Object[] o : result) {
                System.out.println("objects = "+o[0]);
                System.out.println("objects = "+o[1]);
                System.out.println("objects = "+o[2]);
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(Member m1, Member m2) {
        System.out.println("m1 == m2 : " + (m1 instanceof Member));
        System.out.println("m1 == m2 : " + (m2 instanceof Member));
    }
}


