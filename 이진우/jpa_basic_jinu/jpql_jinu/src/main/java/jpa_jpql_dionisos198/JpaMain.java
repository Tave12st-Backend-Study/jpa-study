package jpa_jpql_dionisos198;

import javax.persistence.*;
import java.lang.management.MemoryManagerMXBean;
import java.util.Collection;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
            Team team=new Team();
            em.persist(team);
           Member member1=new Member();
           member1.setUsername("관리자1");
           member1.setTeam(team);
           em.persist(member1);

           Member member2=new Member();
           member2.setUsername("관리자2");
           member2.setTeam(team);
           em.persist(member2);
           em.flush();
           em.clear();

           String query="select m.username from Member m";
           String query2="select m.team from Member m";
           String query3="select t.members from Team t";
           String query4="select m from Team t join t.members m";
           List<Member> result=em.createQuery(query4, Member.class).getResultList();
            System.out.println(result);
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
