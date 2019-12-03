package ee.taltech.iti0203.webstore;

import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.repository.UserRepository;
import ee.taltech.iti0203.webstore.security.Role;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@SpringBootApplication
public class WebStoreApplication {

	@Resource
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(WebStoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner initStore(UserRepository repo) {
		return (args) -> {
			User user = new User();
	    	user.setUsername("user");
	    	user.setPassword(passwordEncoder.encode("user"));
	    	user.setRole(Role.USER);
	    	repo.save(user);
		};
  	}
}
