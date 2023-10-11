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

                    member.changeTeam(team);
                    em.persist(member);


                    em.flush();
                    em.clear();

                    //조인
                    //(1) 내부 조인
                    String query = "select m from Member m inner join m.team t";

                    //(2) 외부조인
                    String query2 = "select m from Member m left outer join m.team t";

                    //(3) 세타조인
                    String query3 = "select m from Member m, Team t where m.username = t.name";

                    //조인-ON절을 활용한 조인
                    //(1) 조인대상 필터링
                    String query4 = "select m from Member m left join m.team t on t.name = 'teamA'";

                    //(2) 연관관계가 없는 엔티티 외부 조인
                    String query5 = "select m from Member m left join Team t on m.username = t.name";

                    List<Member> resultList = em.createQuery(
                            query3
                            , Member.class
                    )
                    .getResultList();


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