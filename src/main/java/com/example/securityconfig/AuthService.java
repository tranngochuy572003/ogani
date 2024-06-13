package com.example.securityconfig;

import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public boolean checkPassword(String email, String rawPassword) {

   try {
     User user = userRepository.findUserByEmail(email);
     if (user != null) {
       return passwordEncoder.matches(rawPassword, user.getPassword());
     }

   }catch (Exception e){
     System.out.println(e.getMessage());
   }

    return false;
  }
}
