package hellojpa;


import javax.persistence.*;
import java.util.List;

public class Main {
            public static void main(String[] args) {

                EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
                EntityManager em = emf.createEntityManager();

                EntityTransaction tx = em.getTransaction();
                tx.begin();
                try{

                    Team team = new Team();
                    team.setName("teamA");
                    em.persist(team);

                    Member member = new Member();
                    member.setUsername("member1");
                    member.setAge(10);
                    member.setMemberType(MemberType.ADMIN);

                    member.changeTeam(team);
                    em.persist(member);


                    em.flush();
                    em.clear();

                    String query =
                            "select "+
                                    "case when m.age<=10 then '학생요금' "+
                                    "     when m.age>=60 then '경로요금' "+
                                    "     else '일반요금' "+
                                    "end "+
                            "from Member m";

                    TypedQuery<String> query1 = em.createQuery(query, String.class);
                    List<String> resultList = query1.getResultList();

                    for(String s : resultList){
                        System.out.println(s);
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