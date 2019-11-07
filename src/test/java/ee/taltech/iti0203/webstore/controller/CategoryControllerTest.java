package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.pojo.CategoryDto;
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
import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {

    @Autowired
    private TestRestTemplate template;

    private static final ParameterizedTypeReference<List<CategoryDto>> LIST_OF_CATEGORIES = new ParameterizedTypeReference<>() {
    };

    @Test
    public void application_returns_list_of_categories() {
        ResponseEntity<List<CategoryDto>> entity = template.exchange("/categories", HttpMethod.GET, null, LIST_OF_CATEGORIES);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<CategoryDto> category = entity.getBody();
        assertTrue(isNotEmpty(category));
        assertTrue(category.stream().anyMatch(p -> p.getName().equals("red")));
    }

    @Test
    public void can_get_category_by_id() {
        ResponseEntity<CategoryDto> entity = template.exchange("/categories/100", HttpMethod.GET, null, CategoryDto.class);
        assertNotNull(entity);
        CategoryDto category = entity.getBody();
        assertNotNull(category);
        assertEquals("green", category.getName());
    }

    @Test
    public void can_add_categories() {
        CategoryDto category = new CategoryDto();
        category.setId(900L);
        category.setName("crimson");
        HttpEntity<CategoryDto> httpEntity = new HttpEntity<>(category);
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, httpEntity, CategoryDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        CategoryDto categoryItem = entity.getBody();
        assertNotNull(categoryItem);
        assertEquals("crimson", categoryItem.getName());
    }

    @Test
    public void can_delete_category_by_id() {
        CategoryDto dummyCategory = new CategoryDto();
        dummyCategory.setName("poisonous");
        dummyCategory.setId(999L);
        ResponseEntity<CategoryDto> entity = template.exchange("/categories", POST, new HttpEntity<>(dummyCategory), CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());

        template.exchange("/categories/999", HttpMethod.DELETE, null, CategoryDto.class);
        entity = template.exchange("/categories/999", HttpMethod.GET, null, CategoryDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertNull(entity.getBody().getName());
    }
}
