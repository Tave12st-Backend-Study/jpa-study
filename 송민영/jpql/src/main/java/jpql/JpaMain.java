package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
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
            member.setUsername("member"+10);
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.setTeam(team);

            em.persist(member);
            em.flush();
            em.clear();

            String query = "select m.username, 'HELLO', TRUE from Member m where  m.type = jpql.MemberType.ADMIN";
            List<Object[]> resultList = em.createQuery( query).getResultList();

            for(Object[]  objects : resultList){
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);
                System.out.println("objects = " + objects[2]);
            }

            tx.commit();
        }
        catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        }
        finally {
            em.close();
        }
        emf.close();

    }



}