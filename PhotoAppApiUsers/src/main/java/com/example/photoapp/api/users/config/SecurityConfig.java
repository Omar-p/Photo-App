package com.example.photoapp.api.users.config;

import com.example.photoapp.api.users.login.AuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

@Configuration
class SecurityConfig {

  Environment environment;

  SecurityConfig(Environment environment) {
    this.environment = environment;
  }


  @Bean
  @DependsOn("authenticationManager")
  SecurityFilterChain securityFilterChain(HttpSecurity http,
                                          AuthenticationFilter authenticationFilter) throws Exception {



    return http
        .authorizeHttpRequests(authorize -> {
          authorize
              .requestMatchers(HttpMethod.POST, "/users", "/users/login")
                .access(new WebExpressionAuthorizationManager("hasIpAddress('"+environment.getProperty("gateway.ip")+"')"))
              .anyRequest().authenticated();
        })
        .addFilter(authenticationFilter)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .csrf(AbstractHttpConfigurer::disable)
        .build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(8);
  }

  @Bean
  @DependsOn("passwordEncoder")
  AuthenticationManager authenticationManager(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)  {
    var daoAuthenticationProvider = new DaoAuthenticationProvider(passwordEncoder);
    daoAuthenticationProvider.setUserDetailsService(userDetailsService);
    return new ProviderManager(daoAuthenticationProvider);
  }
}
