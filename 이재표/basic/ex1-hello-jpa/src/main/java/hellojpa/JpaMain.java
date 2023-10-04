package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import java.util.List;
import java.util.Set;

import static java.time.LocalDateTime.now;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Member member = new Member();
            member.setName("member");
            member.setHomeAddress(new Address("homeCity", "Street", "10000"));

            member.getFavoriteFoods().add("치킨");
            member.getFavoriteFoods().add("피자");
            member.getFavoriteFoods().add("족발");

            member.getAddressHistory().add(new AddressEntity("old1", "Street", "10000"));
            member.getAddressHistory().add(new AddressEntity("old2", "Street", "10000"));
            em.persist(member);

            em.flush();
            em.clear();

            System.out.println("================Start=================");
            Member findMember = em.find(Member.class, member.getId());
            System.out.println("===========값타입 리스트 조회=============");
            List<Address> addressHistory = findMember.getAddressHistory();
//            for (Address address : addressHistory) {
//                System.out.println("address = " + address.getCity());
//            }
//            Set<String> favoriteFoods = findMember.getFavoriteFoods();
//            for (String favoriteFood : favoriteFoods) {
//                System.out.println("foods = "+favoriteFood);
//            }
//            System.out.println("========값타입 수정==========");
//            Member findMember1 = em.find(Member.class, member.getId());
//            Address homeAddress = findMember1.getHomeAddress();
////            findMember1.getHomeAddress().setCity("newCity"); //사이브이펙트 나올수 있음. 이렇게 하면안됨!!
//            findMember1.setHomeAddress(new Address("newCity",homeAddress.getStreet(),homeAddress.getZipcode()));
//
//            //치킨 -> 한식
//            findMember1.getFavoriteFoods().remove("치킨");
//            findMember1.getFavoriteFoods().add("한식");

//            findMember1.getAddressHistory().remove(new Address("old1", "Street", "10000"));
//            findMember1.getAddressHistory().add(new Address("new", "Street", "10000"));

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }

    private static void logic(Member m1, Member m2) {
        System.out.println("m1 == m2 : " + (m1 instanceof Member));
        System.out.println("m1 == m2 : " + (m2 instanceof Member));
    }
}


