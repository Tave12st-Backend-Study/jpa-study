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

                    Team team1 = new Team();
                    team1.setName("Team1");
                    em.persist(team1);

                    Team team2 = new Team();
                    team2.setName("Team2");
                    em.persist(team2);

                    Member member1 = new Member();
                    member1.setUsername("member1");
                    member1.setAge(20);
                    member1.setMemberType(MemberType.ADMIN);

                    member1.changeTeam(team1);
                    em.persist(member1);

                    Member member2 = new Member();
                    member2.setUsername("member2");
                    member2.setAge(20);
                    member2.setMemberType(MemberType.ADMIN);

                    member2.changeTeam(team1);
                    em.persist(member2);


                    Member member3 = new Member();
                    member3.setUsername("member3");
                    member3.setAge(20);
                    member3.setMemberType(MemberType.ADMIN);

                    member3.changeTeam(team1);
                    em.persist(member3);

                    Member member4 = new Member();
                    member4.setUsername("회원4");
                    member4.setAge(20);
                    member4.setMemberType(MemberType.ADMIN);

                    member4.changeTeam(team2);
                    em.persist(member4);

                    Member member5 = new Member();
                    member5.setUsername("회원5");
                    member5.setAge(20);
                    member5.setMemberType(MemberType.ADMIN);

                    member5.changeTeam(team2);
                    em.persist(member5);

                    String query = "select distinct t From Team t join fetch t.members";
                    List<Team> resultList = em.createQuery(query, Team.class).getResultList();

                    for(Object o : resultList){
                        System.out.println(o);
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