package jpabook;

import com.sun.org.apache.xpath.internal.operations.Or;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Order order = new Order();
            LocalDateTime nowDateTime = LocalDateTime.now();
            order.setOrderDate(nowDateTime);
            order.setOrderItems(new OrderItem());
            order.setStatus(OrderStatus.ORDER);
            em.persist(order);

            Member member = new Member();
            member.setName("song1");
            member.setCity("suwon");
            member.setStreet("bongyoungroo");
            member.setZipcode("zipcode1");

            order.setMember(member);
            em.persist(member);

            em.flush();
            em.clear();

//            List<Order> orders = member.getOrders();
//            orders.forEach(order1 -> {
//                order1.setStatus(OrderStatus.CANCEL);
//            });

            tx.commit();
        }catch(Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();

    }
}