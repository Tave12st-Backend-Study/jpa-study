package study.SpringDataJpa.repository.projections;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class UserNameOnlyDto {

    // 생성자에 파라미터 이름으로 매칭을 시켜서 프로젝션을 실행

    private final String username;

    public UserNameOnlyDto(String username) {
        this.username = username;
    }


}
