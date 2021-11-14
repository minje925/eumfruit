package myproject.eumfruit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Controller;

@Configuration
@EnableJpaAuditing  // Spring Data Jpa에서는 Auditing 기능을 제공하여 엔티티가 저장 또는 수정될 때 자동으로 등록일, 수정일, 등록자, 수정자를 입력해준다.
public class AuditConfig {

    @Bean
    public AuditorAware<String> auditorProvider() { // Bean으로 등록한다.
        return new AuditorAwareImpl();
    }
}
