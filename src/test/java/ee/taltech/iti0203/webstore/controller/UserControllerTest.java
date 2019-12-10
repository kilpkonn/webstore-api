package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.pojo.LoginDetails;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.repository.UserRepository;
import ee.taltech.iti0203.webstore.security.JwtTokenProvider;
import ee.taltech.iti0203.webstore.security.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
import static org.springframework.http.HttpMethod.PUT;

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

    @Test
    public void can_register_new_user() {
        UserDto userDto = new UserDto("username", "password");
        ResponseEntity<UserDto> entity = template.exchange("/users/register", POST, new HttpEntity<>(userDto), UserDto.class);
        assertTrue(isNotEmpty(entity));
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        List<User> users = repository.findByUsernameIgnoreCase("username");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("username")));
        repository.delete(users.get(0));
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

        List<User> users = repository.findByUsernameIgnoreCase("mynameis");
        repository.delete(users.get(0));
    }

    @Test
    public void admin_can_change_user_role() {
      UserDto dummyUser = new UserDto("roleuser", "password");
      ResponseEntity<UserDto> entity = template.exchange("/users/register", POST, new HttpEntity<>(dummyUser), UserDto.class);
      assertTrue(isNotEmpty(entity));
      assertTrue(entity.getStatusCode().is2xxSuccessful());

      dummyUser.setRole(Role.UNVERIFIED);
      template.exchange("/users/role", PUT, adminEntity(dummyUser), UserDto.class);

      List<User> users = repository.findByUsernameIgnoreCase("roleuser");
      repository.delete(users.get(0));
      assertTrue(isNotEmpty(users));
      assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("roleuser")));

      assertEquals(Role.UNVERIFIED, users.get(0).getRole());
    }

    @Test
    public void other_roles_cant_change_user_role() {
        UserDto dummyUser = new UserDto("roleuser", "password");
        ResponseEntity<UserDto> entity = template.exchange("/users/register", POST, new HttpEntity<>(dummyUser), UserDto.class);
        assertTrue(isNotEmpty(entity));
        assertTrue(entity.getStatusCode().is2xxSuccessful());

        dummyUser.setRole(Role.ADMIN);
        ResponseEntity<UserDto> response = template.exchange("/users/role", PUT, entity(dummyUser), UserDto.class);
        assertTrue(response.getStatusCode().is4xxClientError());

        List<User> users = repository.findByUsernameIgnoreCase("roleuser");
        repository.delete(users.get(0));

        assertEquals("roleuser", users.get(0).getUsername());
        assertNotEquals(Role.ADMIN, users.get(0).getUsername());
    }

    @Test
    public void cannot_register_null_user() {
        UserDto userDto = new UserDto(null, null);
        ResponseEntity<UserDto> entity = template.exchange("/users/register", POST, new HttpEntity<>(userDto), UserDto.class);
        assertTrue(isNotEmpty(entity));
        assertTrue(entity.getStatusCode().is4xxClientError());
    }

    @Test
    public void cannot_register_double_user() {
        List<User> users = repository.findByUsernameIgnoreCase("abc");
        if (users.size() >= 1) repository.delete(users.get(0));

        UserDto userDto = new UserDto("abc", "pass");
        ResponseEntity<UserDto> entity = template.exchange("/users/register", POST, new HttpEntity<>(userDto), UserDto.class);
        assertTrue(isNotEmpty(entity));
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        users = repository.findByUsernameIgnoreCase("abc");
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("abc")));
        ResponseEntity<UserDto> entity2 = template.exchange("/users/register", POST, new HttpEntity<>(userDto), UserDto.class);
        assertTrue(isNotEmpty(entity2));
        assertTrue(entity2.getStatusCode().is4xxClientError());
    }

    private HttpEntity<UserDto> entity(UserDto userDto) {
        return new HttpEntity<>(userDto, authorizationHeader("user"));
    }

    private HttpEntity<UserDto> adminEntity(UserDto userDto) {
        return new HttpEntity<>(userDto, authorizationHeader("admin"));

    }

    private HttpEntity<UserDto> entity() {
        return new HttpEntity<>(authorizationHeader("user"));
    }

    public HttpHeaders authorizationHeader(String user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtTokenProvider.createTokenForTests(user));
        return headers;
    }
}
