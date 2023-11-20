package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

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


}