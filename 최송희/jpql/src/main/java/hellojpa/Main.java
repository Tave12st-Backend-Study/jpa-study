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
                            "SELECT m FROM Member m", Member.class);
                    TypedQuery<String> query1 = em.createQuery(
                            "SELECT m.username FROM Member m", String.class
                    );

                    //반환타입이 명확하지 않을 때 사용 :: Query
                    Query query2 = em.createQuery(
                            "SELECT m.username, m.age FROM Member m"
                    );

                    //getResultList() :: 결과가 둘 이상일 때(없으면 빈객체 반환) vs getSingleResult() :: 결과가 무조건 하나일때(없으면 에러)
                    List<Member> resultList = query.getResultList();
                    for(Member m : resultList){
                        //하나씩 뽑아와서 출력할 수 있다.
                    }



                    Member singleResult = query.getSingleResult();
                    //없으면  javax.persistence.NoResultException
                    //둘 이상이면  javax.persistence.NonUniqueResultException


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