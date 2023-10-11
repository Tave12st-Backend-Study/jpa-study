package jpabook;

import com.sun.org.apache.xpath.internal.operations.Or;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try{
            //1. JPQL
            List<Member> result  = em.createQuery(
                    "select m From Member m where m.username like '%kim%",
                            Member.class)
                    .getResultList();
            //member 엔티티 자체를 조회해오라는 의미
            //단점 :  동적쿼리를 만들기 어렵다.
            //String sqlString = "select m From Member m" + String where = "where m.username like '%kim%' 이렇게 하기 어려움


            //2.Criteria(망한 스펙, 실무에서 사용하지 않는 스펙)
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Member> query = cb.createQuery(Member.class);
            //Member에서
            Root<Member> m = query.from(Member.class);
            //m을 select, m의 username이 kim인 것을 추출
            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));

            List<Member> resultList = em.createQuery(cq).getResultList();

            //장점 : 동적쿼리가 가능/sql 작성을 올바르게 작성해주도록 컴파일시점에 오타를 잡아준다.
            //단점 : sql 스럽지 않다. 실무에서는 안쓴다. 유지보수가 어렵다.


            //3. QueryDSL(Criteria 대안책)
            //오픈소스 라이브러리
            //JPQL빌더역할/컴파일 시점에 문법오류 찾기 가능/자바코드로 JPQL작성가능
            //실무사용 권장/단순하고 쉬움
            //동적쿼리에서 막강한 힘을 제공
            //JPQL만 잘하면 QueryDSL를 잘하게 되어있다.

            //4. 네이티브 SQL(잘 안씀)
            em.createNativeQuery(
                    "select MEMBER_ID, city, street. zipcode, USERNAME from MEMBER"
            ).getResultList();


            //5. JDBC
            //조심해야할 점 : 영속성 컨텍스트는 플러시가 되어야 db에 데이터가 있다.

            //(AUTO전략)flush되는 시점 : commit되기 전, query날아가기 전에 동작
            // => flush되면 DB에 적용된다.

            //예
            Member member =  new Member();
            member.setUsername("member1");
            em.persist(member);

            //이 시점에 em.flush()가 자동으로 날아가 DB에 insert해준다.

            List<Member> resultList1 = em.createNativeQuery(
                    "select MEMBER_ID, city, street, zipcode FROM MEMBER"
            ).getResultList();

            // 반면 JDBC는 jpa가 아니므로 다음과 같이 작성해준다면
            //em.flush() x -> DB에 값이 없다. , 오류 발생
            //수동으로 em.flush() 작서해야 한다.

            //dbconn.executeQuery("select * from member");


            //결론!! JPQL 정말 철저히 잘하기

            tx.commit();
        }catch(Exception e){
            tx.rollback();
            e.printStackTrace();
        }finally {
            em.close();
        }
        emf.close();

    }
    private static void logic(Member m1, Member m2){
        System.out.println("m1 :"+(m1 instanceof Member));
        System.out.println("m1 :"+(m2 instanceof Member));

    }
}