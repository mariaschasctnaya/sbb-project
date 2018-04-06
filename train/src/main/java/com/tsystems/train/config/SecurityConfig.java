package com.tsystems.train.config;

import com.tsystems.train.handler.AuthenticationHandler;
import com.tsystems.train.service.UserService;
import com.tsystems.train.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
//This element is used to enable annotation-based protection in the application.
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //The configure (HttpSecurity) method determines which path URLs should be protected and which ones do not
                .authorizeRequests()
                .antMatchers("/passengers", "/routes", "/stations", "/trains")
                .hasRole("MANAGER")
                .and()
                .formLogin()
                // default spring-security URL for processing authorization data.
                .loginProcessingUrl("/j_spring_security_check")
                // specify the page with the login form
                .loginPage("/login")
                .successHandler(new AuthenticationHandler())
                .and()
                .logout()
                // specify the logout URL
                .logoutUrl("/logout")
                // specify the URL with a successful logout
                .logoutSuccessUrl("/")
                // do not valid current session
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .and()
                // enable protection against CSRF attacks
                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        //This requests do not need to be secured
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/images/**");
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth)  throws Exception {

        auth.userDetailsService(userService)
                .passwordEncoder(passwordEncoder());
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
