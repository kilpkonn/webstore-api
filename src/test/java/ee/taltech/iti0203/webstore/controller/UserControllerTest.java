package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.pojo.LoginDetails;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.repository.UserRepository;
import ee.taltech.iti0203.webstore.security.JwtTokenProvider;
import ee.taltech.iti0203.webstore.security.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate template;

    @Autowired
    UserRepository repository;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    private static final ParameterizedTypeReference<List<UserDto>> LIST_OF_USERS = new ParameterizedTypeReference<>() {
    };

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void can_register_new_user() {
        UserDto userDto = new UserDto("username", "password");
        ResponseEntity<UserDto> entity = template.exchange("/users/register", POST, new HttpEntity<>(userDto), UserDto.class);
        assertTrue(isNotEmpty(entity));
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        List<User> users = repository.findByUsernameIgnoreCase("username");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("username")));
    }

    @Test
    public void user_can_login() {
        UserDto userDto = new UserDto("mynameis", "password");
        ResponseEntity<UserDto> response = template.exchange("/users/register", POST, new HttpEntity<>(userDto), UserDto.class);
        assertTrue(isNotEmpty(response));
        assertTrue(response.getStatusCode().is2xxSuccessful());

        ResponseEntity<LoginDetails> entity = template.exchange("/users/login", POST, new HttpEntity<>(userDto), LoginDetails.class);
        assertTrue(isNotEmpty(entity));
        assertTrue(isNotEmpty(entity.getBody()));
        LoginDetails details = entity.getBody();
        assertEquals(details.getUsername(), userDto.getUsername());
        assertEquals(details.getRole(), Role.USER);
        assertTrue(isNotEmpty(details.getToken()));
    }

    private HttpEntity<UserDto> entity(UserDto userDto) {
        return new HttpEntity<>(userDto, authorizationHeader());
    }

    private HttpEntity<UserDto> entity() {
        return new HttpEntity<>(authorizationHeader());
    }

    public HttpHeaders authorizationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtTokenProvider.createTokenForTests("user"));
        return headers;
    }
}
