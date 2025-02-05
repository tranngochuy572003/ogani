package com.example.securityconfig;


import com.example.exception.BadRequestException;
import com.example.service.impl.UserServiceImpl;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private UserServiceImpl userServiceImpl;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {
    try {
      String jwt = getJwtFromRequest(request);

      String email;

      if (StringUtils.hasText(jwt) && validateToken(jwt)) {
        try {
          email = getUserNameFromJWT(jwt);
        } catch (ParseException e) {
          throw new RuntimeException(e);
        }

        UserDetails userDetails = userServiceImpl.loadUserByUsername(email);
        if (userDetails != null) {
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  private boolean validateToken(String token) {

    Boolean check = false;
    String[] chunks = token.split("\\.");
    if (chunks.length != 3) {
      throw new IllegalArgumentException("Invalid JWT token.");
    }
    return true;
  }

  private String getUserNameFromJWT(String token) throws ParseException {
    if(validateToken(token)){
      Base64.Decoder decoder = Base64.getUrlDecoder();
      String[] chunks = token.split("\\.");

      String header = new String(decoder.decode(chunks[0]));
      String payload = new String(decoder.decode(chunks[1]));

      JWT jwt = JWTParser.parse(token);
      JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
      return (String) claimsSet.getClaim("userName");

    }
    else {
      throw new BadRequestException("Invalid token");
    }


  }


}
