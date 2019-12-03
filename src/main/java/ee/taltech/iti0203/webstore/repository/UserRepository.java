package ee.taltech.iti0203.webstore.repository;

import ee.taltech.iti0203.webstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUsername(String username);
}
