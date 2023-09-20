package jpa_book_dionisos198;

import jpa_book_dionisos198.domain.Item;
import jpa_book_dionisos198.domain.Member;
import jpa_book_dionisos198.domain.Order;
import jpa_book_dionisos198.domain.OrderItem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    public static void main(String[] args){
        EntityManagerFactory emf= Persistence.createEntityManagerFactory("hello");
        EntityManager em=emf.createEntityManager();
        EntityTransaction tx=em.getTransaction();
        tx.begin();
        try{
            Item item=new Item();
            item.setName("A");
            item.setPrice(3000);
            item.setStockQuantity(15);
            em.persist(item);
            System.out.println(item.getName().length());


            OrderItem orderItem=new OrderItem();
            orderItem.setItem(item);
            em.persist(orderItem);
            Member member=new Member();
            member.setName("jinu");
            em.persist(member);

            Order order=new Order();
            order.addOrderItem(orderItem);
            order.setMember(member);

            em.persist(order);


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
