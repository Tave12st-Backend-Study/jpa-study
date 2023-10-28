package hellojpa;


import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class Main {
            public static void main(String[] args) {

                EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
                EntityManager em = emf.createEntityManager();

                EntityTransaction tx = em.getTransaction();
                tx.begin();
                try{

                    Team teamA=new Team();
                    teamA.setName("팀A");
                    em.persist(teamA);

                    Team teamB=new Team();
                    teamB.setName("팀B");
                    em.persist(teamB);

                    for(int i=0;i<50;i++){
                        Member member=new Member();
                        member.setUsername("회원"+(i+1));
                        member.setTeam(teamA);
                        em.persist(member);
                    }
                    for(int i=0;i<50;i++){
                        Member member=new Member();
                        member.setUsername("회원"+(i+51));
                        member.setTeam(teamB);
                        em.persist(member);
                    }

                    em.flush();
                    em.clear();
                    System.out.println("======================");
                    List<Team> teams=em.createQuery("select t from Team t", Team.class).getResultList();
                    for (Team team : teams) {
                        team.getName();
                        for(Member mem:team.getMembers()){
                            mem.getUsername();
                        }
                    }

                    tx.commit();


                }catch(Exception e){
                    tx.rollback();
                    e.printStackTrace();
                }finally {
                    em.close();
                }
                emf.close();

    }
}