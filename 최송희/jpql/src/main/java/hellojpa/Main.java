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

                    Member member =  new Member();
                    member.setUsername("member1");
                    member.setAge(10);
                    em.persist(member);

                    em.flush();
                    em.clear();

                    //1. 엔티티 프로젝션
                    List<Member> result = em.createQuery(
                            "SELECT m FROM Member m", Member.class
                    ).getResultList();

                    Member findMember = result.get(0);
                    findMember.setAge(20);
                    //UPDATE쿼리 발생..JPA에 의해 관리됨을 알 수 있다.


                    //2. 엔티티 프로젝션
                    List<Team> result2 = em.createQuery(
                            "SELECT t FROM Member m JOIN m.team t", Team.class
                    ).getResultList();
                    //Member객체에 Team이 포함되어있어도 이렇게 해서 team과 조인하도록 작성 추천
                    //쿼리 날아가는 것도 확인해보면 join됨을 알 수 있다.


                    //3. 임베디드 타입 프로젝션
                    em.createQuery(
                            "SELECT o.address FROM Order o", Address.class
                    ).getResultList();

                    //4. 스칼라 타입 프로젝션
                    //(1) Query타입으로 조회하는 경우
                    List resultList = em.createQuery(
                            "select m.username, m.age from Member m"
                    ).getResultList();

                    Object o = resultList.get(0);
                    Object[] result3 = (Object[]) o;
                    System.out.println("username = "+result3[0]);
                    System.out.println("age = "+result3[1]);

                    //(2) Object[] 타입으로 조회하는 경우
                    List <Object[]> resultList1 = em.createQuery(
                            "select m.username, m.age from Member m"
                    ).getResultList();

                    Object[] result4 = resultList1.get(0);
                    System.out.println("username = "+result4[0]);
                    System.out.println("age = "+result4[1]);

                    //(3) new 명령어로 조회하는 경우
                    List<MemberDTO> resultList2 = em.createQuery(
                            "select new hellojpa.MemberDTO(m.username, m.age) from Member m"
                            , MemberDTO.class
                    ).getResultList();

                    MemberDTO memberDTO = resultList2.get(0);
                    System.out.println("username :: "+memberDTO.getUsername());



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