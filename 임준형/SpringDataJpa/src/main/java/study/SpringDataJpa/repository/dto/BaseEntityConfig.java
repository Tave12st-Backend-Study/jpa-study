package study.SpringDataJpa.repository.dto;

import java.util.Optional;
import java.util.UUID;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Configuration
public class BaseEntityConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // 지금은 UUID이지만, 실제 개발에선 spring Securitycontextholder 에서 값을 꺼낸다.
        return () -> Optional.of(UUID.randomUUID().toString());
    }
}
