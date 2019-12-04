package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.UserExistsException;
import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.repository.UserRepository;
import ee.taltech.iti0203.webstore.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserDto saveUser(UserDto userDto) {
        if (!isEmpty(userRepository.findByUsernameIgnoreCase(userDto.getUsername()))) {
            throw new UserExistsException();
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);
        return userDto;
    }
}
