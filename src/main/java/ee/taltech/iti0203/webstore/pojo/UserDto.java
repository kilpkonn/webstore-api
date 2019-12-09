package ee.taltech.iti0203.webstore.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    // @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private Role role;

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserDto(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
    }
}
