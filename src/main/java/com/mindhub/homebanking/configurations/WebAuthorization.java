package com.mindhub.homebanking.configurations;
import com.mindhub.homebanking.Utils.CustomAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;


@EnableWebSecurity
@Configuration
public class WebAuthorization extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/clients","/api/activateAccount/**").permitAll()
                .antMatchers("/web/index.html", "/web/login.html","/web/401.html","/web/403.html", "/web/style/**", "/web/script/**", "/web/img/**", "/web/assets/**", "/web/activateClient.html").permitAll()
                .antMatchers("/api/clients/current/**").hasAuthority("CLIENT")
                .antMatchers("/rest/**", "/h2-console", "/api/**", "/web/cryptos.html").hasAuthority("ADMIN")
                .antMatchers("/**").hasAnyAuthority("CLIENT", "ADMIN");

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login")
                .defaultSuccessUrl("/web/accounts.html",true)
                .failureHandler((request, response, exception) -> {
                    response.setStatus(900);
                });

        http.logout()
                .logoutUrl("/api/logout")
                .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        http.exceptionHandling()
                                .authenticationEntryPoint(((request, response, authException) ->                {
                                    if(request.getRequestURI().contains("/web") && !request.getRequestURI().contains("login")){
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
