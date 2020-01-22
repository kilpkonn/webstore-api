package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.repository.CategoryRepository;
import ee.taltech.iti0203.webstore.security.JwtTokenProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CategoryRepository repository;

    private static final ParameterizedTypeReference<List<CategoryDto>> LIST_OF_CATEGORIES = new ParameterizedTypeReference<>() {
    };

    @Before
    public void setUp() {
        repository.deleteAll();  // Just in case

        repository.save(new Category("Category 1"));
        repository.save(new Category("Category 2"));
        repository.save(new Category("Category 3"));
        repository.save(new Category("Category 4"));
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void application_returns_list_of_categories() {
        ResponseEntity<List<CategoryDto>> entity = template.exchange("/categories", HttpMethod.GET, entity(), LIST_OF_CATEGORIES);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<CategoryDto> category = entity.getBody();
        assertTrue(isNotEmpty(category));
        assertTrue(category.stream().anyMatch(p -> p.getName().equals("Category 1")));
        assertTrue(category.stream().anyMatch(p -> p.getName().equals("Category 3")));
    }

    @Test
    public void can_get_category_by_id() {
        CategoryDto category = new CategoryDto();
        category.setName("Category 6");
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, entity(category), CategoryDto.class);
        assertNotNull(entity.getBody());
        entity = template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.GET, entity(),
                CategoryDto.class);
        assertNotNull(entity);
        category = entity.getBody();
        assertNotNull(category);
        assertEquals("Category 6", category.getName());
    }

    @Test
    public void can_add_categories() {
        CategoryDto category = new CategoryDto();
        category.setName("Category 5");
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, entity(category), CategoryDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        CategoryDto categoryItem = entity.getBody();
        assertNotNull(categoryItem);
        assertEquals("Category 5", categoryItem.getName());
    }

    @Test
    public void can_delete_category_by_id() {
        CategoryDto dummyCategory = new CategoryDto();
        dummyCategory.setName("Category 3");
        dummyCategory.setId(3L);
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, entity(dummyCategory), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertNotNull(entity.getBody().getName());

        template.exchange(String.format("/categories/%d", entity.getBody().getId()), HttpMethod.DELETE, entity(),
                CategoryDto.class);
        entity = template.exchange(String.format("/categories/%d", entity.getBody().getId()), HttpMethod.GET, entity(), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertNull(entity.getBody().getName());
    }

    @Test
    public void can_rename_category() {
        CategoryDto dummyCategory = new CategoryDto();
        dummyCategory.setName("oldName");
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, entity(dummyCategory), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());

        entity = template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.GET, entity(), CategoryDto.class);

        assertNotNull(entity.getBody());
        CategoryDto renamedDummyCategory = new CategoryDto();
        renamedDummyCategory.setName("newName");
        renamedDummyCategory.setId(entity.getBody().getId());

        template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.PUT, entity(renamedDummyCategory),
                CategoryDto.class);
        entity = template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.GET, entity(), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertEquals("newName", entity.getBody().getName());
    }

    @Test
    public void can_delete_category() {
        CategoryDto dummyCategory = new CategoryDto();
        dummyCategory.setName("oldName");
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, entity(dummyCategory), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());

        template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.DELETE, entity(), CategoryDto.class);

        entity = template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.GET, entity(), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertNull(entity.getBody().getName());
    }

    private HttpEntity<CategoryDto> entity(CategoryDto categoryDto) {
        return new HttpEntity<>(categoryDto, authorizationHeader());
    }

    private HttpEntity<CategoryDto> entity() {
        return new HttpEntity<>(authorizationHeader());
    }

    public HttpHeaders authorizationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtTokenProvider.createTokenForTests("user"));
        return headers;
    }
}
