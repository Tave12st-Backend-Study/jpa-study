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
                    member.setUsername("관리자");
                    member.setAge(10);
                    member.setMemberType(MemberType.ADMIN);

                    member.changeTeam(team);
                    em.persist(member);

                    em.flush();
                    em.clear();

                    String query = "select NULLIF(m.username, '관리자') from Member m";
                    List<String> resultList = em.createQuery(query, String.class).getResultList();

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