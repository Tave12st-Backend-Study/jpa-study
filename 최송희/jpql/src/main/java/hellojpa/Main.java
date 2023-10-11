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

                    for(int i=0;i<100;i++) {
                        Member member = new Member();
                        member.setUsername("member"+(i+1));
                        member.setAge(i);
                        em.persist(member);
                    }

                    em.flush();
                    em.clear();


                    List<Member> resultList2 = em.createQuery(
                            "select m from Member m order by m.age desc"
                            , Member.class
                    ).setFirstResult(0)
                    .setMaxResults(10)
                    .getResultList();

                    System.out.println("size : "+ resultList2.size());
                    for(Member m : resultList2){
                        System.out.println(m);
                    }

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