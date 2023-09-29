package com.example.jpashop;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static javax.persistence.Persistence.createEntityManagerFactory;

@SpringBootApplication
public class JpashopApplication {

    public static void main(String[] args) {
        EntityManagerFactory emf = createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            Book book = new Book();
            book.setName("jpa");
            book.setAuthor("김영한");
            em.persist(book);

            tx.commit();
        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }
        emf.close();
    }

}
