package net.atos.spring_webapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

//@EnableOAuth2Sso
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private DataSource dataSource;

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .usersByUsernameQuery("SELECT u.email, u.password, u.enable FROM user u WHERE u.email = ?")
                .authoritiesByUsernameQuery("SELECT u.email, p.role_name FROM user u JOIN user_permission up ON up.user_id = u.user_id JOIN permission p ON p.permission_id = up.permission_id WHERE u.email = ?")
                .dataSource(dataSource).passwordEncoder(bCryptPasswordEncoder);
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/post**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN") //hasAnyRole -> bez prefixu ROLE_ / lepiej stosować takie nazewnictwo bo nawet google api stosuje takie
                .antMatchers("/post/delete").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .antMatchers("/post&edit&*").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .antMatchers("/editpost").hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                .anyRequest().permitAll()
                .and().csrf().disable()
                .formLogin().loginPage("/login")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .loginProcessingUrl("/login_process")
                    .failureUrl("/login_error")
                    .defaultSuccessUrl("/")
                .and()
//                .oauth2Login().loginPage("/login_google")
//                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/");

    }
}
