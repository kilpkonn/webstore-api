package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.exception.MyBadRequestException;
import ee.taltech.iti0203.webstore.pojo.LoginDetails;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.security.*;
import ee.taltech.iti0203.webstore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

  @Secured({Roles.ROLE_UNVERIFIED, Roles.ROLE_USER, Roles.ROLE_ADMIN})
  @GetMapping
  public List<UserDto> users() {
    return userService.getUsers();
  }

  @Secured(Roles.ROLE_ADMIN)
  @PutMapping("role")
  public void changeRole(@RequestBody UserDto userDto) {
    userService.changeRole(userDto);
  }

  @PostMapping("register")
  public void register(@RequestBody UserDto userDto) {
    if (userDto.getUsername() == null || userDto.getPassword() == null) {
      throw new MyBadRequestException();
    }
    userService.saveUser(userDto);
  }

  @PostMapping("login")
  public LoginDetails login(@RequestBody UserDto userDto) {
    if (userDto.getUsername() == null || userDto.getPassword() == null) {
      throw new MyBadRequestException();
    }
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
    final UserDetails userDetails = myUserDetailsService.loadUserByUsername(userDto.getUsername());
    final String token = jwtTokenProvider.generateToken(userDetails);
    MyUser myUser = (MyUser) userDetails;
    return new LoginDetails(myUser.getUsername(), token, toAuthorities(myUser), myUser.getRole());
  }

  private List<String> toAuthorities(MyUser myUser) {
    return myUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
  }
}
