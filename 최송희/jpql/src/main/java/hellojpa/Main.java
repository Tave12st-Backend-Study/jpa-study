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

                    Team teamA = new Team();
                    teamA.setName("팀A");
                    em.persist(teamA);

                    Team teamB = new Team();
                    teamB.setName("팀B");
                    em.persist(teamB);

                    Member member1 = new Member();
                    member1.setUsername("회원1");
                    member1.setAge(20);
                    member1.setMemberType(MemberType.ADMIN);

                    member1.changeTeam(teamA);
                    em.persist(member1);

                    Member member2 = new Member();
                    member2.setUsername("회원2");
                    member2.setAge(20);
                    member2.setMemberType(MemberType.ADMIN);

                    member2.changeTeam(teamA);
                    em.persist(member2);


                    Member member3 = new Member();
                    member3.setUsername("회원3");
                    member3.setAge(20);
                    member3.setMemberType(MemberType.ADMIN);

                    member3.changeTeam(teamB);
                    em.persist(member3);

                    em.flush();
                    em.clear();

                    String jpql = "select distinct t from Team t join fetch t.members";
                    List<Team> teamList = em.createQuery(jpql, Team.class).getResultList();

                    for(Team team : teamList) {
                        System.out.println("teamname = " + team.getName() + ", team = " + team);
                        for (Member member : team.getMembers()) {
                            //페치 조인으로 팀과 회원을 함께 조회해서 지연 로딩 발생 안함
                            System.out.println("-> username = " + member.getUsername()+ ", member = " + member);
                        }
                    }


                }catch(Exception e){
                    tx.rollback();
                    e.printStackTrace();
                }finally {
                    em.close();
                }
                emf.close();

    }
}