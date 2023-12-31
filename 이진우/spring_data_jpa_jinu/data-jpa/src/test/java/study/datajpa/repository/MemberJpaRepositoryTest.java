package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.lang.management.MemoryManagerMXBean;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Autowired
    EntityManager em;

    @Test
    public void testMember(){
        Member member=new Member("memberA");
        Member savedMember=memberJpaRepository.save(member);

        Member findMember=memberJpaRepository.find(savedMember.getId());

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        Assertions.assertThat(findMember).isEqualTo(member);



    }


    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberJpaRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        long count = memberJpaRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }


    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1= new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);

        List<Member> result=
                memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA",15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);


    }

    @Test
    public void paging() throws Exception {
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age=10;
        int offset=0;
        int limit=3;

        List<Member> members = memberJpaRepository.findByPage(age,offset,limit);
        long totalCount=memberJpaRepository.totalCount(age);

        Assertions.assertThat(members.size()).isEqualTo(3);
        Assertions.assertThat(totalCount).isEqualTo(5);



    }

    @Test
    public void bulkUpdate() throws Exception{
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",19));
        memberJpaRepository.save(new Member("member3",20));
        memberJpaRepository.save(new Member("member4",21));
        memberJpaRepository.save(new Member("member5",40));

        int resultCount = memberJpaRepository.bulkAgePlus(20);

        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void jpaEventBaseEntity() throws Exception{
        Member member=new Member("member1");
        memberJpaRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        Member findMember=memberJpaRepository.findById(member.getId()).get();

        System.out.println("findMember.createdDate = "+findMember.getCreatedDate());
       // System.out.println("findMember.updatedDate = "+findMember.getUpdatedDate());


    }


}