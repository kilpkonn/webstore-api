package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.User;
import ee.taltech.iti0203.webstore.pojo.LoginDetails;
import ee.taltech.iti0203.webstore.pojo.UserDto;
import ee.taltech.iti0203.webstore.pojo.UserInfoDto;
import ee.taltech.iti0203.webstore.repository.UserRepository;
import ee.taltech.iti0203.webstore.security.JwtTokenProvider;
import ee.taltech.iti0203.webstore.security.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate template;

    @Autowired
    UserRepository repository;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    private static final ParameterizedTypeReference<List<UserInfoDto>> LIST_OF_USERS = new ParameterizedTypeReference<>() {
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
        assertEquals(details.getRole(), Role.UNVERIFIED);
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

        dummyUser.setRole(Role.USER);
        template.exchange("/users/role", PUT, adminEntity(dummyUser), UserDto.class);

        List<User> users = repository.findByUsernameIgnoreCase("roleuser");
        assertTrue(isNotEmpty(users));
        assertTrue(users.stream().anyMatch(u -> u.getUsername().equals("roleuser")));

        assertEquals(Role.USER, users.get(0).getRole());
        repository.delete(users.get(0));
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

    @Test
    public void can_get_users() {
        repository.findAll().forEach(System.out::println);
        ResponseEntity<List<UserInfoDto>> entity = template.exchange("/users", GET, entity(), LIST_OF_USERS);
        assertTrue(isNotEmpty(entity));
        assertTrue(entity.getStatusCode().is2xxSuccessful());
        assertNotNull(entity.getBody());
        assertTrue(entity.getBody().size() > 0);
        assertNotNull(entity.getBody().get(0).getId());
        assertNotNull(entity.getBody().get(0).getRole());
        assertNotNull(entity.getBody().get(0).getUsername());
    }

    @Test
    public void admin_can_delete_user() {
        UserDto dummyUser = new UserDto("deleteduser", "password");
        ResponseEntity<UserDto> entity = template.exchange("/users/register", POST, new HttpEntity<>(dummyUser), UserDto.class);
        assertTrue(isNotEmpty(entity));
        assertTrue(entity.getStatusCode().is2xxSuccessful());

        List<User> users = repository.findByUsernameIgnoreCase("deleteduser");
        Long id = users.get(0).getId();
        template.exchange("/users/" + id, DELETE, adminEntity(), UserDto.class);

        users = repository.findByUsernameIgnoreCase("deleteduser");
        assertTrue(users.isEmpty());
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

    private HttpEntity<UserDto> adminEntity() {
        return new HttpEntity<>(authorizationHeader("admin"));
    }

    public HttpHeaders authorizationHeader(String user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtTokenProvider.createTokenForTests(user));
        return headers;
    }
}
