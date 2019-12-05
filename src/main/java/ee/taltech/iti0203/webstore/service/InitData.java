package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.repository.UserRepository;
import ee.taltech.iti0203.webstore.security.Role;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("test")
@AllArgsConstructor
public class InitData implements CommandLineRunner {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        User user = new User();
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setRole(Role.USER);
        userRepository.save(user);
    }
}
