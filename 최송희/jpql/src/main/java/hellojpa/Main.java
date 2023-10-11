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
                    //파라미터 바인딩하지 않은 경우
                    String query = "select m.username, 'HELLO', true from Member m "+
                            "where m.type = hellojpa.MemberType.USER";

                    //파라미터 바인딩을 한 경우
                    String query2 = "select m.username, 'HELLO', true from Member m "+
                            "where m.type = :usertype";

                    List<Object[]> result = em.createQuery(query)
                            .getResultList();

                    List<Object[]> result2 = em.createQuery(query2)
                            .setParameter("usertype", MemberType.ADMIN)
                            .getResultList();

                    for(Object[] objects: result){
                        System.out.println("objects = "+objects[0]);
                        System.out.println("objects = "+objects[1]);
                        System.out.println("objects = "+objects[2]);
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