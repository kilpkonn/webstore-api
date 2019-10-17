package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.pojo.ProductDto;
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

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.POST;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @Autowired
    private TestRestTemplate template;

    private static final ParameterizedTypeReference<List<ProductDto>> LIST_OF_PRODUCTS = new ParameterizedTypeReference<>() {
    };

    @Test
    public void application_returns_list_of_products() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products", HttpMethod.GET, null, LIST_OF_PRODUCTS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Daffodil")));
    }

    @Test
    public void can_get_product_by_id() {
        ResponseEntity<ProductDto> entity = template.exchange("/products/1", HttpMethod.GET, null, ProductDto.class);
        assertNotNull(entity);
        ProductDto product = entity.getBody();
        assertNotNull(product);
        assertEquals("Agapanthus", product.getName());
    }

    @Test
    public void cant_get_product_by_unknown_id() {
        ResponseEntity<ProductDto> entity = template.exchange("/products/90210", HttpMethod.GET, null, ProductDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    public void can_search_products_by_name() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products?name=carn", HttpMethod.GET, null, LIST_OF_PRODUCTS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertEquals(1, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Carnation")));
    }

    @Test
    public void can_search_products_by_category() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products?category=red", HttpMethod.GET, null, LIST_OF_PRODUCTS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Chrysanthemum")));
    }

    @Test
    public void search_by_name_returns_nothing_if_not_found() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products?name=carburetor", HttpMethod.GET, null, LIST_OF_PRODUCTS);
        assertNotNull(entity);
        List<ProductDto> products = entity.getBody();
        assertTrue(isEmpty(products));
    }

    @Test
    public void can_add_products() {
        ProductDto product = new ProductDto();
        product.setName("TestProduct");
        product.setDescription("Random");
        product.setAmount(10);
        product.setPrice(5.0);
        HttpEntity<ProductDto> httpEntity = new HttpEntity<>(product);
        ResponseEntity<ProductDto> entity = template.exchange("/products", POST, httpEntity, ProductDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        ProductDto prod = entity.getBody();
        assertNotNull(prod);
        assertEquals("TestProduct", prod.getName());
    }
}
