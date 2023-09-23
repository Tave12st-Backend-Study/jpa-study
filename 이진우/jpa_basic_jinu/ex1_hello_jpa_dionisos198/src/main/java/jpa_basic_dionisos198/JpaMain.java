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


             Member member=new Member();
              member.setName("member1");
              //member.setTeam(team);
             em.persist(member);
            System.out.println("======================");

            Team team=new Team();
            team.setName("teamA");
            team.addMember(member);
            em.persist(team);
            System.out.println("=====================");

             em.flush();
             em.clear();


             Team findTeam=em.find(Team.class,team.getId());
             List<Member> members=findTeam.getMembers();

            for (Member member1 : members) {
                System.out.println(member1.getName());
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
