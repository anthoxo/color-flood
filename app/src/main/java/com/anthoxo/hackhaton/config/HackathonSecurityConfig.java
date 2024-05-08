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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.Arrays;
import java.util.Collections;

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
                    mvcMatcherBuilder.pattern("/api/users"),
                    mvcMatcherBuilder.pattern("/api/codes/**"),
                    mvcMatcherBuilder.pattern("/api/game/**"),
                    mvcMatcherBuilder.pattern("/api/game/run/**"),
                    mvcMatcherBuilder.pattern("/api/ladder/**")
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

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(
                Arrays.asList("http://localhost:4200")
        );
        configuration.setAllowedMethods(
                Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
        );
        configuration.addAllowedHeader("Content-Type");
        configuration.addExposedHeader("Content-Disposition");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
