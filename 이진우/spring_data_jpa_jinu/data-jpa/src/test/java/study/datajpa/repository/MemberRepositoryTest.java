package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.swing.plaf.metal.MetalMenuBarUI;
import java.lang.management.MemoryManagerMXBean;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member=new Member("memberA");
        Member savedMember=memberRepository.save(member);

        Member findMember=memberRepository.findById(savedMember.getId()).get();

        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        Assertions.assertThat(findMember).isEqualTo(member);



    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        Assertions.assertThat(findMember1).isEqualTo(member1);
        Assertions.assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        Assertions.assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        Assertions.assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        Assertions.assertThat(deletedCount).isEqualTo(0);
    }


    @Test
    public void findByUsernameAndAgeGreaterThan(){
        Member m1= new Member("AAA",10);
        Member m2 = new Member("AAA",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result= memberRepository.findByUsernameAndAgeGreaterThan("AAA",15);
        Assertions.assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        Assertions.assertThat(result.get(0).getAge()).isEqualTo(20);
        Assertions.assertThat(result.size()).isEqualTo(1);


    }

    @Test
    public void testQuery(){
        Member m1=new Member("AAA",10);
        Member m2=new Member("BBB",20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result= memberRepository.findUser("AAA",10);
        Assertions.assertThat(result.get(0)).isEqualTo(m1);


    }


    @Test
    public void findUsernameList(){
        Member m1=new Member("AAA",10);
        Member m2= new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();

        for (String s : usernameList) {
            System.out.println("s = "+ s);
        }
    }

    @Test
    public void findMemberDto(){
        Team team=new Team("teamA");
        teamRepository.save(team);

        Member m1=new Member("AAA",10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto=memberRepository.findMemberDto();

        for (MemberDto dto : memberDto) {
            System.out.println("dto = "+dto);
        }
    }

    @Test
    public void findByNames(){
        Member m1=new Member("AAA",10);
        Member m2= new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA","BBB"));
        for (Member member : result) {
            System.out.println("member = "+ member);
        }
    }

    @Test
    public void returnType(){
        Member m1=new Member("AAA",10);
        Member m2= new Member("BBB",20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> aaa = memberRepository.findListByUsername("AAA");
        for (Member member : aaa) {
            System.out.println(aaa);
        }

    }

    @Test
    public void page() throws Exception{
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
        Page<Member> page = memberRepository.findByAge(10,pageRequest);



        List<Member> content=page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void pageJoin() throws Exception{
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
        Page<Member> page = memberRepository.findJoinByAge(10,pageRequest);



        List<Member> content=page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void slice() throws Exception{
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",10));
        memberRepository.save(new Member("member3",10));
        memberRepository.save(new Member("member4",10));
        memberRepository.save(new Member("member5",10));

        PageRequest pageRequest = PageRequest.of(0,3, Sort.by(Sort.Direction.DESC,"username"));
        Slice<Member> page = memberRepository.findSliceByAge(10,pageRequest);



        List<Member> content=page.getContent();
        Assertions.assertThat(content.size()).isEqualTo(3);
        //Assertions.assertThat(page.getTotalElements()).isEqualTo(5);
        Assertions.assertThat(page.getNumber()).isEqualTo(0);
        //Assertions.assertThat(page.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(page.isFirst()).isTrue();
        Assertions.assertThat(page.hasNext()).isTrue();

    }

    @Test
    public void bulkUpdate() throws Exception{
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));

        int resultCount = memberRepository.bulkAgePlus(20);

        Member findMember=memberRepository.findMemberByUsername("member5");

        System.out.println("age="+findMember.getAge());

        Assertions.assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        //given
        //member1 -> teamA
        //member2 -> teamB
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        memberRepository.save(new Member("member1", 10, teamA));
        memberRepository.save(new Member("member2", 20, teamB));
        em.flush();
        em.clear();
        //when
        List<Member> members = memberRepository.findAll();
        //then
        for (Member member : members) {
            member.getTeam().getName();
        }
    }

    @Test
     public void queryHint() throws Exception{
        memberRepository.save(new Member("member1",10));
        em.flush();
        em.clear();

        Member member=memberRepository.findReadOnlyByUsername("member1");
        member.setUsername("member2");

        em.flush();
    }

    @Test
    public void custom(){
        List<Member> result=memberRepository.findMemberCustom();
    }


    @Test
    public void jpaEventBaseEntity() throws Exception{
        Member member=new Member("member1");
        memberRepository.save(member);

        Thread.sleep(100);
        member.setUsername("member2");

        em.flush();
        em.clear();

        Member findMember=memberRepository.findById(member.getId()).get();

        System.out.println("findMember.createdDate = "+findMember.getCreatedDate());
         System.out.println("findMember.updatedDate = "+findMember.getLastModifiedDate());
        System.out.println("findMember.createdBy = "+findMember.getCreatedBy());
        System.out.println("findMember.updatedModifiedBy = "+findMember.getLastModifiedBy());


    }
    @Test
    public void nativeQuery(){
        Team teamA = new Team("teamA");
        em.persist(teamA);

        Member m1=new Member("m1",0,teamA);
        Member m2=new Member("m2",0,teamA);

        em.persist(m1);
        em.persist(m2);

        em.flush();
        em.clear();

        int age=memberRepository.findByNativeQuery("m1");
        System.out.println("result= "+age);




    }



}