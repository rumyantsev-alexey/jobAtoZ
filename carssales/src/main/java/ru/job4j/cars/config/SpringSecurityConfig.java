package ru.job4j.cars.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.job4j.cars.security.UserDetailServiceImpl;

@Configuration
@EnableWebSecurity
@Order(2)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailServiceImpl userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        .antMatchers("/cars/login", "/cars/list", "/cars/models", "/cars/image").permitAll()
        .antMatchers("/cars/add").authenticated()
        .anyRequest().authenticated()
        .and()
        .csrf().disable()
        .formLogin()
        .loginPage("/cars/login")
        .loginProcessingUrl("/cars/login/process")
        .defaultSuccessUrl("/cars/list")
        .failureUrl("/cars/login?errors")
        .usernameParameter("login")
        .passwordParameter("pass")
        .and()
        .logout()
        .logoutSuccessUrl("/cars/login");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
