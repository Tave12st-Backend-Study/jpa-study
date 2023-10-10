package jpa_jpql_dionisos198;

import javax.persistence.*;
import java.lang.management.MemoryManagerMXBean;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
           Team team=new Team();
           team.setName("teamA");
           em.persist(team);

           Member member=new Member();
           member.setUsername("member");
           member.setAge(10);
           member.changeTeam(team);
           member.setType(MemberType.ADMIN);
            em.persist(member);
            em.flush();
            em.clear();

            String query="select m.username,'HELLO',TRUE From Member m where m.type=jpa_jpql_dionisos198.MemberType.ADMIN";
            List<Object[]> result=em.createQuery(query).getResultList();
            for (Object[] objects : result) {
                System.out.println("objects = "+objects[0]);
                System.out.println("objects = "+objects[1]);
                System.out.println("objects = "+objects[2]);

            }
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
