package study.SpringDataJpa.repository.projections;

import org.springframework.beans.factory.annotation.Value;

public interface UserNameOnly {

    // openProjections
    // username 와 age를 하나의 string으로 더해서 보여
    @Value("#{target.username + ' ' + target.age}")
    String getUsername();
}
