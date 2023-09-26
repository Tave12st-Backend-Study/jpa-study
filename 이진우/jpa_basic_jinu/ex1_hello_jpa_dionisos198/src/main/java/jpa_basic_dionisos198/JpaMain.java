package jpa_basic_dionisos198;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
            System.out.println("===========0============");
             Member member=new Member();
              member.setName("member1");
              //member.setTeam(team);
             em.persist(member);
            System.out.println("===========1===========");

            Team team=new Team();
            team.setName("teamA");
            team.addMember(member);
            em.persist(team);
            System.out.println("===========2==========");

             em.flush();
             em.clear();

            System.out.println("=============3========");
             Team findTeam=em.find(Team.class,team.getId());
            System.out.println("===========4============");
          tx.commit();
            System.out.println("===========5============");

        }catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
