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

                    //이 사이에서 flush가 자동으로 일어난다,
                    //(flush 자동으로 일어나는 조건 : commit되기 전 or 쿼리가 실행되기 전)

                    //db에 직접 벌크연산(한번에 update or delete 연산이 나가는 것) 발생
                    //벌크연산일때는 영속성 컨텍스트에 반영되지 않음
                    int resultCount = em.createQuery("update Member m set m.age = 20")
                            .executeUpdate();
                    System.out.println("resultCount = "+resultCount); //3 출력

                    //영속성 컨텍스트에는 반영이 안되어있음(clear을 해야 데이터가 날아가는데
                    //flush한다고 해서 데이터가 없어지는 게 아님)

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