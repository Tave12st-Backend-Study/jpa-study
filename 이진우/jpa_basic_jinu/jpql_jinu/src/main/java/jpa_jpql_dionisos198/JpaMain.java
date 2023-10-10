package jpa_jpql_dionisos198;

import javax.persistence.*;
import java.lang.management.MemoryManagerMXBean;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
            for(int i=0;i<100;i++) {
                Member member = new Member();
                member.setUsername("member"+i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

            List<Member> result=em.createQuery("select m from Member m order by m.age desc",Member.class)
                            .setFirstResult(1)
                                    .setMaxResults(10).getResultList();
            System.out.println("result.size ="+result.size());
            for (Member member1 : result) {
                System.out.println("member1 = "+member1);
            }
            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }
        finally {
            em.close();
        }

        emf.close();
    }
}
