package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.repository.CategoryRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

    @Autowired
    private TestRestTemplate template;

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
        ResponseEntity<List<CategoryDto>> entity = template.exchange("/categories", HttpMethod.GET, null, LIST_OF_CATEGORIES);
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
        HttpEntity<CategoryDto> httpEntity = new HttpEntity<>(category);
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, httpEntity, CategoryDto.class);
        assertNotNull(entity.getBody());
        entity = template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.GET, null,
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
        HttpEntity<CategoryDto> httpEntity = new HttpEntity<>(category);
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, httpEntity, CategoryDto.class);
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
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, new HttpEntity<>(dummyCategory), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertNotNull(entity.getBody().getName());

        template.exchange("/categories/3", HttpMethod.DELETE, null, CategoryDto.class);
        entity = template.exchange("/categories/3", HttpMethod.GET, null, CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertNull(entity.getBody().getName());
    }

    @Test
    public void can_rename_category() {
        CategoryDto dummyCategory = new CategoryDto();
        dummyCategory.setName("oldName");
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, new HttpEntity<>(dummyCategory), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());

        entity = template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.GET, null, CategoryDto.class);

        assertNotNull(entity.getBody());
        CategoryDto renamedDummyCategory = new CategoryDto();
        renamedDummyCategory.setName("newName");
        renamedDummyCategory.setId(entity.getBody().getId());

        template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.PUT, new HttpEntity<>(renamedDummyCategory),
                CategoryDto.class);
        entity = template.exchange("/categories/" + entity.getBody().getId(), HttpMethod.GET, null, CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertEquals("newName", entity.getBody().getName());
    }
}
