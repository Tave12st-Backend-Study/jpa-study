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


                    //1
                    String jpql = "select distinct t from Team t join fetch t.members";
//                    List<Team> teamList = em.createQuery(jpql, Team.class)
//                            .setFirstResult(0)
//                            .setMaxResults(1)
//                            .getResultList();

                    //일대다 페치조인 주의할 점
                    //- 둘 이상의 컬렉션은 페치조인할 수 없다.
                    //- 컬렉션을 페치조인하면 데이터 뻥튀기.. 한번에 데이터 조회후,
                    //  메모리에서 페이징 API가 적용되므로 경고로그가 나온다.(매우 위험)

                    //+ 별칭 사용 불가
                    String jpqls = "select t From Team t join fetch t.members m where m.age>10";

                    //2
                    String jpqlDv = "select m from Member m join fetch m.team t";
                   // 위 일대다 페치조인 페이징 API의 대안 : 다대일 페치조인으로 뒤집어서 페이징 api 적용


                    //3
                    String jpqlDv2 = "select t from Team t"; //+ BatchSize(size = 100)정도
                    List<Team> teamList = em.createQuery(jpqlDv2, Team.class)
                            .getResultList();

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