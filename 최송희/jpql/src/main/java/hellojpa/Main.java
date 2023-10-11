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
                    em.persist(member);

                    //반환 타입이 명확할때 사용 :: TypedQuery<제네릭>
                    TypedQuery<Member> query = em.createQuery("" +
                            "SELECT m FROM Member m WHERE m.username = :username", Member.class);
                    //파라미터 바인딩- 이름기준일때
                    query.setParameter("username", "member1");
                    Member singleResult = query.getSingleResult();
                    System.out.println("singleResult = "+singleResult);

                    //파라미터 바인딩-위치기준(추천 X)

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