package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.exception.MyBadRequestException;
import ee.taltech.iti0203.webstore.pojo.LoginDetails;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.security.JwtTokenProvider;
import ee.taltech.iti0203.webstore.security.MyUser;
import ee.taltech.iti0203.webstore.security.MyUserDetailsService;
import ee.taltech.iti0203.webstore.security.UserSessionHolder;
import ee.taltech.iti0203.webstore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

  private UserService userService;
  private AuthenticationManager authenticationManager;
  private MyUserDetailsService myUserDetailsService;
  private JwtTokenProvider jwtTokenProvider;

  @PostMapping("register")
  public void register(@RequestBody UserDto userDto) {
    if (userDto.getUsername() == null) {
      throw new MyBadRequestException();
    }
    if (userDto.getPassword() == null) {
      throw new MyBadRequestException();
    }
    userService.saveUser(userDto);
  }

  @PostMapping("login")
  public LoginDetails login(@RequestBody UserDto userDto) {
    if (userDto.getUsername() == null) {
      throw new MyBadRequestException();
    }
    if (userDto.getPassword() == null) {
      throw new MyBadRequestException();
    }
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(userDto.getUsername());
    final String token = jwtTokenProvider.generateToken(userDetails);
    MyUser myUser = (MyUser) userDetails;
    return new LoginDetails(myUser.getUsername(), token, toAuthorities(myUser), myUser.getRole());
  }

  @GetMapping("me")
  public MyUser me() {
    return UserSessionHolder.getLoggedInUser();
  }

  private List<String> toAuthorities(MyUser myUser) {
    return myUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
  }
}
