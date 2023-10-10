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
            em.persist(member);
            em.flush();
            em.clear();

           String query="select m from Member m join m.team t";
           String query2="select m from Member m LEFT JOIN m.team t";
           String query3="select m from Member m, Team t where m.username=t.name";
           String query4="select m from Member m left join m.team t on t.name='teamA'";
           String query5="select m from Member m left join Team t on m.username=t.name";
           List<Member> result=em.createQuery(query5,Member.class).getResultList();
            System.out.println("result = "+result.size());
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
