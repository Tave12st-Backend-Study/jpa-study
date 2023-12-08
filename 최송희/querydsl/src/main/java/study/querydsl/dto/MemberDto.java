package study.querydsl.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MemberDto
{
    private String username;
    private int age;

    @QueryProjection //QMemberDto가 생성된다.
    public MemberDto(String username, int age){
        this.username = username;
        this.age = age;
    }

    public MemberDto() {
    }
}
