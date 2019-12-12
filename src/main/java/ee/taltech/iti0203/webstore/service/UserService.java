package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.MyBadRequestException;
import ee.taltech.iti0203.webstore.exception.UserExistsException;
import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.pojo.UserInfoDto;
import ee.taltech.iti0203.webstore.repository.UserRepository;
import ee.taltech.iti0203.webstore.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public List<UserInfoDto> getUsers() {
        return userRepository.findAll().stream().map(UserInfoDto::new).collect(Collectors.toList());
    }

    public void changeRole(UserDto userDto) {
        List<User> existing = userRepository.findByUsernameIgnoreCase(userDto.getUsername());
        if (isEmpty(existing)) {
            throw new MyBadRequestException();
        }
        for (User user : existing) {
            User newUser = new User(userDto);
            user.setRole(newUser.getRole());
            userRepository.save(user);
        }
    }

    public UserDto saveUser(UserDto userDto) {
        if (!isEmpty(userRepository.findByUsernameIgnoreCase(userDto.getUsername()))) {
            throw new UserExistsException();
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.UNVERIFIED);
        userRepository.save(user);
        return userDto;
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
