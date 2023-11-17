package com.example.photoapp.api.users.login;

import com.example.photoapp.api.users.AppUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;

@Component
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final UserLoginService userLoginService;
  private final Environment environment;
  public AuthenticationFilter(AuthenticationManager authenticationManager, UserLoginService userLoginService, Environment environment) {
    super(authenticationManager);
    this.userLoginService = userLoginService;
    this.environment = environment;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      final UserLoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), UserLoginRequest.class);
      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              credentials.email(),
              credentials.password(),
              Collections.emptyList()
          )
      );
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
    final String email = userLoginService.getEmailFromPrincipal(authResult.getPrincipal());
    final AppUser appUser = userLoginService.findAppUserByEmail(email);
    var tokenSecret = environment.getProperty("token.secret");
    var secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
    SecretKey secretkey = new SecretKeySpec(secretKeyBytes, Jwts.SIG.HS512.key().build().getAlgorithm());


    var now = Instant.now();

    String token = Jwts.builder()
        .subject(appUser.userId().toString())
        .expiration(Date.from(now.plusMillis(Long.parseLong(environment.getProperty("token.expiration_time")))))
        .issuedAt(Date.from(now))
        .signWith(secretkey)
        .compact();

    response.addHeader("Authorization", "Bearer " + token);


  }

  @PostConstruct
  void init() {
    setFilterProcessesUrl(environment.getProperty("login.url.path"));
  }
}
