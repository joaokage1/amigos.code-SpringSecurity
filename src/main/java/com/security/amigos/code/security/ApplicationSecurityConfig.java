package com.security.amigos.code.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.security.amigos.code.security.ApplicationUserPermission.STUDENT_WRITE;
import static com.security.amigos.code.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@Data
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll()
                .antMatchers("/api/v1/students/*").hasRole(STUDENT.name())
                .antMatchers(HttpMethod.DELETE,"/management/api/v1/students/**").hasAuthority(STUDENT_WRITE.name())
                .antMatchers(HttpMethod.POST,"/management/api/v1/students/**").hasAuthority(STUDENT_WRITE.name())
                .antMatchers(HttpMethod.PUT,"/management/api/v1/students/**").hasAuthority(STUDENT_WRITE.name())
                .antMatchers(HttpMethod.GET,"/management/api/v1/students/**").hasAnyRole(ADMIN.name(), ADMINTRAINEE.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("james")
                .password(getPasswordEncoder().encode("1234"))
                .roles(STUDENT.name()) //ROLE_STUDENT
                .build();

        UserDetails admin =User.builder()
                .username("admin")
                .password(getPasswordEncoder().encode("admin"))
                .roles(ApplicationUserRole.ADMIN.name())
                .build();

        UserDetails admintrainee =User.builder()
                .username("admin_trainee")
                .password(getPasswordEncoder().encode("admin"))
                .roles(ApplicationUserRole.ADMINTRAINEE.name())
                .build();

        return new InMemoryUserDetailsManager(
                user,
                admin,
                admintrainee
        );
    }
}
