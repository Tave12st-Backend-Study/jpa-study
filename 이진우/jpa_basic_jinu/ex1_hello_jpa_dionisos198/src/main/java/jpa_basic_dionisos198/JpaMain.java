package jpa_basic_dionisos198;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
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

           Member member1=new Member();
           member1.setName("member1");
           member1.setTeam(team);
           em.persist(member1);

           em.flush();
           em.clear();
            System.out.println("---");
           Member m=em.find(Member.class,member1.getId());
           System.out.println("m = "+m.getTeam().getClass());

            System.out.println("==============");
            System.out.println("teamName= "+m.getTeam().getName());
            System.out.println("==============");
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
