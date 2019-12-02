package ee.taltech.iti0203.webstore.pojo;

import ee.taltech.iti0203.webstore.security.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class LoginDetails {

    private String username;
    private String token;
    private List<String> roles;
    private Role role;

}
