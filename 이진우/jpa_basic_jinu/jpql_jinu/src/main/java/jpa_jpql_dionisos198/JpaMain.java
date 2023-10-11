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

          String query="select m from Member m";
          String query2="select m from Member m join fetch m.team t";
           List<Member> result=em.createQuery(query2, Member.class).getResultList();
            for (Member member : result) {
                System.out.println(member.getUsername()+"  "+member.getTeam().getName());
            }
            em.flush();
            em.clear();;
            System.out.println("================================");
            String query3="select t from Team t";
            String query4="select distinct t from Team t join fetch t.members m";
            String query5="select t from Team t join t.members m";
            List<Team> result2=em.createQuery(query5,Team.class).getResultList();
            for (Team team : result2) {
                System.out.println(team.getName()+" "+team.getMembers().size());
                for(Member member9:team.getMembers()){
                    System.out.println(member9.getUsername());
                }
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
