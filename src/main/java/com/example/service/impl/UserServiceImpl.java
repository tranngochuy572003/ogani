package com.example.service.impl;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.securityconfig.CustomUserDetails;
import com.example.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

@Data
public class UserServiceImpl implements UserService, UserDetailsService {

  private UserRepository userRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }


  @Override
  public void addUser(User user) {
    if (user.getPassword().isEmpty() || user.getUserName().isEmpty() || user.getFullName().isEmpty()) {
      throw new BadRequestException("Field required is not blank.");
    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setPassword(encoder.encode(user.getPassword()));
    userRepository.save(user);
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public User getUserById(String id) {
    Optional<User> user = userRepository.findById(id);
    if (user.isPresent()) {
      return user.get();
    } else {
      throw new com.example.exception.BadRequestException("Email or Password is invalid");
    }
  }


  @Override
  public void updateEmailUser(String id, UserDto userDto) {
    Optional<User> optionalUser = userRepository.findUserById(id);

    if (userRepository.existsByUserName(userDto.getUserName())) {
      throw new BadRequestException("UserName existed");
    }

    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      user.setUserName(userDto.getUserName());

      userRepository.save(user);
    } else {
      throw new com.example.exception.BadRequestException("Id is invalid");
    }


  }

  @Override
  public void deleteUser(String userId) {
    Optional<User> optionalUser = userRepository.findById(userId);
    if (optionalUser.isPresent()) {
      userRepository.deleteById(userId);
    } else {
      throw new com.example.exception.BadRequestException("Id is invalid");
    }
  }

  @Override
  public void updateUser(String id, UserDto userDto) {
    Optional<User> optionalUser = userRepository.findUserById(id);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();

      User userSaved = UserMapper.toEntity(user, userDto);

      BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
      user.setPassword(encoder.encode(user.getPassword()));

      userRepository.save(userSaved);
    } else {
      throw new com.example.exception.BadRequestException("Id is invalid");
    }
  }

  @Override
  public User findUserByEmail(String email) {
    User user = userRepository.findUserByEmail(email);

    if (user == null) {
      throw new com.example.exception.BadRequestException("Email is invalid");
    }
    return user;
  }

  @Override
  public boolean checkUserExist(String username) {
    if (userRepository.existsByUserName(username)) {
      throw new BadRequestException("UserName existed");
    }
    return true;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = userRepository.findUserByEmail(email);
    if (user == null) {
      throw new UsernameNotFoundException(email);
    }

    return new CustomUserDetails(user);
  }
}

