package myproject.eumfruit.config;

import myproject.eumfruit.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity// WebSecurityConfiguration를 상속받는 클래스에 이 어노테이션을 선언하면 SpringSecurityFilterChain이 자동으로 포함된다.
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /* http 요청에 대한 보안을 설정한다. 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을
        작성한다.
         */
        http.formLogin()
                .loginPage("/members/login")    // 로그인 페이지  url
                .defaultSuccessUrl("/") // 로그인 성공시 url
                .usernameParameter("email") // 로그인시 사용할 파라미터 이름으로 email 사용
                .failureUrl("/members/login/error") // 로그인 실패시 이동할 url
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 url
                .logoutSuccessUrl("/")
        ; // 로그아웃 성공 시 이동할 url

        http.authorizeRequests()    // 시큐리티 처리에 HttpServletRequest를 이용한다
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**", "/fonts/**", "/picture/**").permitAll() // permitAll()을 통해 모든 사용자가 인증(로그인)없이 경로에 접근할 수 있도록 설정한다. 메인페이지, 회원관련url, 상품페이지 경로 등이 이에 해당한다.
                .mvcMatchers("/admin/**").hasRole("ADMIN")  // /admin으로 시작하는 경로는 해당 계정이 ADMIN Role일 경우에만 접근가능하도록 설정한다.
                .anyRequest().authenticated() // 위에서 설정한 경로등은 모두 인증을 요구하도록 설정한다.
        ;

        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        ;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*
        Spring Security에서 인증은 AuthenticationManager를 통해 이루어지며 AuthenticationManagerBuilder가
        AuthenticationManager를 생성한다. userDetailService를 구현하고 있는 객체로 memberService를 지정해주며
        비밀버호 암호화를 위해 passwordEncoder를 지정해준다.
         */
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**"); // static 디렉터리의 하위 파일은 인증을 무시하도록 설정한다.
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
