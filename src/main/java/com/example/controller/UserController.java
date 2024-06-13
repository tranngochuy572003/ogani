package com.example.controller;

import com.example.dto.UserDto;
import com.example.dto.UserDtoLogin;
import com.example.entity.User;
import com.example.securityconfig.AuthService;
import com.example.securityconfig.JwtTokenProvider;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.common.*;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserServiceImpl userServiceImpl;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;
  @Autowired
  private AuthService authService;


  @PostMapping("/login")
  public ResponseEntity<?> userLogin(@RequestBody UserDtoLogin userDtoLogin) {

    boolean isAuthenticated = authService.checkPassword(userDtoLogin.getUserName(), userDtoLogin.getPassword());

    try {
      if (isAuthenticated) {
        String jwtToken = jwtTokenProvider.createToken(userDtoLogin);
        return new ResponseEntity<>(jwtToken, HttpStatus.OK);
      }else {
        return new ResponseEntity<>(EMAIL_PASSWORD_INVALID, HttpStatus.BAD_REQUEST);
      }
    } catch (Exception e) {
      return new ResponseEntity<>(UNAUTHORIZED, HttpStatus.UNAUTHORIZED);
    }

  }


  @PostMapping("/add")
  public ResponseEntity<String> addUser(@RequestBody User user) {
    if(userServiceImpl.checkUserExist(user.getUserName())){
      userServiceImpl.addUser(user);
    }
    return new ResponseEntity<>(ITEM_CREATED_SUCCESS, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<User>> userList() {
    List<User> users = userServiceImpl.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<User> getUser(@PathVariable String id) {
    User user = userServiceImpl.getUserById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PatchMapping("/updateEmail/{id}")
  public ResponseEntity<String> updateEmailUser(@PathVariable String id, @RequestBody UserDto userDto) {
    userServiceImpl.updateEmailUser(id, userDto);
    return new ResponseEntity<>(FIELD_UPDATED_SUCCESS, HttpStatus.OK);
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
    userServiceImpl.updateUser(id, userDto);
    return new ResponseEntity<>(ITEM_UPDATED_SUCCESS, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    userServiceImpl.deleteUser(id);
    return new ResponseEntity<>(ITEM_DELETED_SUCCESS, HttpStatus.OK);
  }

}
