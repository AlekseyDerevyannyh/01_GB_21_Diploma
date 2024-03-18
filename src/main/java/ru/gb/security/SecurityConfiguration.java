package ru.gb.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(registry -> registry
                        .requestMatchers("/role/**", "/user/**").hasAuthority("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.GET,"/task")).authenticated()
                        .requestMatchers(antMatcher(HttpMethod.POST,"/task")).hasAuthority("USER_ISSUING")
                        .requestMatchers(antMatcher(HttpMethod.GET,"/task/**")).authenticated()
                        .requestMatchers(antMatcher(HttpMethod.DELETE,"/task/**")).hasAuthority("ADMIN")
                        .requestMatchers(antMatcher(HttpMethod.PUT,"/task/**/accept")).hasAuthority("USER_ACCEPTING")
                        .requestMatchers(antMatcher(HttpMethod.PUT,"/task/**/cancel")).hasAuthority("USER_ISSUING")
                        .requestMatchers(antMatcher(HttpMethod.PUT,"/task/**/complete")).hasAuthority("USER_ACCEPTING")
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}
