package ee.taltech.iti0203.webstore.pojo;

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
public class UserInfoDto {
    private Long id;
    private String username;
    private Role role;

    public UserInfoDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
