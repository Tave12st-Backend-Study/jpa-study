package study.SpringDataJpa.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import study.SpringDataJpa.entity.Member;

@Data
public class MemberDto {

    private Long id;
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {
        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.teamName = "teamA";
    }

    public static MemberDto toMemberDto(Member member) {
        return new MemberDto(
                member.getId(), member.getUsername(), "team");
    }
}
