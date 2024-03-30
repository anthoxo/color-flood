package com.anthoxo.hackhaton.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class HackathonSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      HandlerMappingIntrospector introspector
  ) throws Exception {
    MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(
        introspector
    );
    http
        .httpBasic(b -> b.disable())
        .csrf(c -> c.disable())
        .authorizeHttpRequests(c ->
            c
                .requestMatchers(
                    mvcMatcherBuilder.pattern("/api/users")
                )
                .permitAll()
                .requestMatchers(
                    mvcMatcherBuilder.pattern("/api/**")
                )
                .authenticated()
        )
        .cors(Customizer.withDefaults())
        .sessionManagement(s ->
            s.sessionCreationPolicy(SessionCreationPolicy.NEVER)
        );
    return http.build();
  }
}
