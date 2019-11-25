package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.exception.MyBadRequestException;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.security.MyUser;
import ee.taltech.iti0203.webstore.security.UserSessionHolder;
import ee.taltech.iti0203.webstore.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

	private UserService userService;
	private AuthenticationManager authenticationManager;

	@PostMapping("register")
	public UserDto register(@RequestBody UserDto userDto) {
		if (userDto.getUsername() == null) {
			throw new MyBadRequestException();
		}
		if (userDto.getPassword() == null) {
			throw new MyBadRequestException();
		}
		userService.saveUser(userDto);
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		return userDto;
	}

	@PostMapping("login")
	public UserDto login(@RequestBody UserDto userDto) {
		if (userDto.getUsername() == null) {
			throw new MyBadRequestException();
		}
		if (userDto.getPassword() == null) {
			throw new MyBadRequestException();
		}
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
		return userDto;
	}

	@GetMapping("me")
	public MyUser me() {
		return UserSessionHolder.getLoggedInUser();
	}
}


