package jpa_basic_dionisos198;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf=Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
           Child child1=new Child();
           Child child2=new Child();

           Parent parent=new Parent();
           parent.addChild(child1);
           parent.addChild(child2);

           em.persist(parent);
           em.flush();
           em.clear();
           Parent parent1=em.find(Parent.class,parent.getId());
           parent1.getChildList().remove(0);
       /*    em.persist(child1);
           em.persist(child2);*/
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
