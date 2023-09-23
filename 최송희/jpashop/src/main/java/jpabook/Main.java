package jpabook;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Team team = new Team();
            team.setName("teamB");
            em.persist(team);

            Member member= new Member();
            member.setUsername("sonGg");
            member.setTeam(team);
            //연관관계 주인에서 관리하게 되면 TEAM, MEMBER DB에도 값이 세팅된다.
            /*
            !주의!
            * team.getMember().add(member) : MEMBER DB에 TEAM_ID가 null이 된다.
            * */
            em.persist(member);

            em.flush();
            em.clear();

            Team team1 = em.find(Team.class, team.getId());
            List<Member> members = team1.getMembers();
            for(Member m : members){
                System.out.println(m.getUsername());
            }

            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
}