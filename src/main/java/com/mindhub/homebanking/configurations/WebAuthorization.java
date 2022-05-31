package com.mindhub.homebanking.configurations;
import com.mindhub.homebanking.Utils.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/clients").permitAll()
                .antMatchers("/web/index.html", "/web/login.html","/web/401.html","/web/403.html", "/web/style/**", "/web/script/**", "/web/img/**", "/web/assets/**").permitAll()
                .antMatchers("/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers("/rest/**", "/h2-console", "/api/**", "/web/cryptos.html").hasAuthority("ADMIN")
                .antMatchers("/**").hasAnyAuthority("CLIENT", "ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login")
                .defaultSuccessUrl("/web/accounts.html",true)
                .failureHandler((request, response, exception) -> response.setStatus(-200));

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        http.exceptionHandling()
                                .authenticationEntryPoint(((request, response, authException) ->                {
                                    if(request.getRequestURI().contains("/web") && !request.getRequestURI().contains("login")){
                                            System.out.println(request.getRequestURI());
                                            response.sendRedirect("/web/401.html");
                                    }}))
                                .accessDeniedHandler(accessDeniedHandler());

        http.csrf().disable();
        http.headers().frameOptions().disable();
    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

}
