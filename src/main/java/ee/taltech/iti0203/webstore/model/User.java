package ee.taltech.iti0203.webstore.model;

import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "webstore")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.setUsername(userDto.getUsername());
        this.setPassword(userDto.getPassword());
        this.setRole(userDto.getRole());
    }
}
