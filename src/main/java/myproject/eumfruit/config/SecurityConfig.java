package myproject.eumfruit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity// WebSecurityConfiguration를 상속받는 클래스에 이 어노테이션을 선언하면 SpringSecurityFilterChain이 자동으로 포함된다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* http 요청에 대한 보안을 설정한다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을
        작성한다.
         */
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        /*
        비밀번호를 DB에 귿로 저장했을 경우, 회원의 정보가 그대로 노출된다.
        이를 해결하기 위해 BCryptPasswordEncoder() 해시 함수를 이용하여 비밀번호를 암호화하여 저장한다.
        BCryptPasswordEncoder를 빈으로 등록하여 사용할 것이다.
         */
        return new BCryptPasswordEncoder();
    }
}
