package jpa_jpql_dionisos198;

import javax.persistence.*;
import java.lang.management.MemoryManagerMXBean;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
            
            Team teamC=new Team();
            teamC.setName("팀C");
            em.persist(teamC);
            
            Team teamD=new Team();
            teamD.setName("팀D");
            em.persist(teamD);
            
            Team teamE=new Team();
            teamE.setName("팀E");
            em.persist(teamE);
            
            for(int i=0;i<4;i++){
                Member member=new Member();
                member.setUsername("멤버"+(i+1));
                member.setTeam(teamA);
                em.persist(member);
            }
            for(int i=0;i<3;i++){
                Member member=new Member();
                member.setUsername("멤버"+(i+5));
                member.setTeam(teamB);
                em.persist(member);
            }
            for(int i=0;i<2;i++){
                Member member=new Member();
                member.setUsername("멤버"+(i+8));
                member.setTeam(teamC);
                em.persist(member);
            }
            for(int i=0;i<1;i++){
                Member member=new Member();
                member.setUsername("멤버"+(i+10));
                member.setTeam(teamD);
                em.persist(member);
            }
            Member member=new Member();
            member.setUsername("멤버11");
            member.setTeam(teamE);
            em.persist(member);
            
            em.flush();
            em.clear();

            List<Team> teams=em.createQuery("select t from Team t", Team.class).getResultList();
            for (Team team : teams) {
                System.out.println("team:"+team.getName());
                for(Member mem:team.getMembers()){
                    System.out.println(mem.getUsername());
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
