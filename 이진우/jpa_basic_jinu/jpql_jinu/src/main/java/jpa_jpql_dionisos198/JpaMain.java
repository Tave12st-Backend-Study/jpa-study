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
            Team teamA=new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB=new Team();
            teamB.setName("팀B");
            em.persist(teamB);


            Member member1=new Member();
           member1.setUsername("회원1");
           member1.setTeam(teamA);
           em.persist(member1);

           Member member2=new Member();
           member2.setUsername("회원2");
           member2.setTeam(teamA);
           em.persist(member2);

            Member member3=new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);



           em.flush();
           em.clear();

            String query="select m from Member m where m=:member";
            String query2="select m from Member m where m.id=:memberId";
            String query3="select m from Member m where m.team=:team";
            List<Member> members=em.createQuery(query3,Member.class).setParameter("team",teamA).getResultList();
            System.out.println(members);
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
