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

                    Member member1 = new Member();
                    member1.setUsername("member1");
                    member1.setAge(20);
                    member1.setMemberType(MemberType.ADMIN);

                    em.persist(member1);

                    member1.setAge(10);

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