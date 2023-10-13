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

                    Member member2 = new Member();
                    member2.setUsername("member2");
                    member2.setAge(20);
                    member2.setMemberType(MemberType.ADMIN);

                    em.persist(member2);


                    Member member3 = new Member();
                    member3.setUsername("member3");
                    member3.setAge(20);
                    member3.setMemberType(MemberType.ADMIN);

                    em.persist(member3);

                    Member member4 = new Member();
                    member4.setUsername("회원4");
                    member4.setAge(20);
                    member4.setMemberType(MemberType.ADMIN);

                    em.persist(member4);

                    int updateNUM = em.createQuery(
                                    "UPDATE Member m SET m.username= 'songhee' where m.username = :name")
                            .setParameter("name", "회원4")
                            .executeUpdate();

                    em.clear();


                    Member findMember = em.find(Member.class, member4.getId());
                    System.out.println("findMember name = " + findMember.getUsername());

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