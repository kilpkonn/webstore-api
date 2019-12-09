package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.repository.CategoryRepository;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import ee.taltech.iti0203.webstore.security.JwtTokenProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.POST;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ProductControllerTest {
    @Autowired
    private TestRestTemplate template;

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;  // Needed to test product categories

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    private static final ParameterizedTypeReference<List<ProductDto>> LIST_OF_PRODUCTS = new ParameterizedTypeReference<>() {
    };

    @Before
    public void setUp() {
        // Just in case
        repository.deleteAll();
        categoryRepository.deleteAll();

        var p1 = new Product("Agapanthus", "Agapanthus desc", 5, 20.55);
        var p2 = new Product("Bells of Ireland", "Bells of Ireland desc", 10, 9.99);
        var p3 = new Product("Carnation", "Carnation desc", 10, 9.11);
        var p4 = new Product("Daffodil", "Daffodil desc", 3, 3.50);
        var p5 = new Product("Ornithogalum", "Ornithogalum desc", 101, 6.50);
        var p6 = new Product("Violet", "Violet desc", 0, 2.20);
        var p7 = new Product("Chrysanthemum", "Carnation desc", 10, 2.99);

        var cat1 = new Category("red");
        p3.setCategory(cat1);
        p7.setCategory(cat1);

        var cat2 = new Category("green");
        p2.setCategory(cat2);

        var cat3 = new Category("blue");
        p1.setCategory(cat3);

        categoryRepository.saveAll(Arrays.asList(cat1, cat2, cat3));
        repository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void application_returns_list_of_products() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products", HttpMethod.GET, entity(), LIST_OF_PRODUCTS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Daffodil")));
    }

    @Test
    public void can_get_product_by_id() {
        ProductDto product = new ProductDto();
        product.setName("TestProduct");
        product.setDescription("Random");
        product.setAmount(10);
        product.setPrice(5.0);
        ResponseEntity<ProductDto> entity = template.exchange("/products", POST, entity(product), ProductDto.class);
        assertNotNull(entity.getBody());

        entity = template.exchange("/products/" + entity.getBody().getId(), HttpMethod.GET, entity(), ProductDto.class);
        assertNotNull(entity);
        product = entity.getBody();
        assertNotNull(product);
        assertEquals("TestProduct", product.getName());
    }

    @Ignore
    @Test
    public void can_get_product_image_by_id() {
        ProductDto product = new ProductDto();
        product.setName("Randomname");
        product.setDescription("Random");
        product.setAmount(10);
        product.setPrice(5.0);
        product.setImageUrl("placeholder.jpg");
        ResponseEntity<ProductDto> entity = template.exchange("/products", POST, entity(product), ProductDto.class);
        assertNotNull(entity.getBody());

        ResponseEntity<InputStreamResource> entity2 = template.exchange(String.format("/products/%d/image", entity.getBody().getId()), HttpMethod.GET, entity(), InputStreamResource.class);
        assertEquals(HttpStatus.OK, entity2.getStatusCode());
        InputStreamResource image = entity2.getBody();
        assertNotNull(image);
        assertTrue(image.exists());
    }

    @Test
    public void cant_get_product_by_unknown_id() {
        ResponseEntity<ProductDto> entity = template.exchange("/products/90210", HttpMethod.GET, entity(), ProductDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
    }

    @Test
    public void can_search_products_by_name() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products?name=carn", HttpMethod.GET, entity(), LIST_OF_PRODUCTS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertEquals(1, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Carnation")));
    }

    @Test
    public void can_search_products_by_category() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products?category=red", HttpMethod.GET, entity(), LIST_OF_PRODUCTS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertEquals(2, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Chrysanthemum")));
    }

    @Test
    public void can_search_products_by_name_and_category() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products?name=bells&category=green", HttpMethod.GET, entity(), LIST_OF_PRODUCTS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertEquals(1, products.size());
        assertTrue(products.stream().anyMatch(p -> p.getName().equals("Bells of Ireland")));
    }

    @Test
    public void search_by_name_returns_nothing_if_not_found() {
        ResponseEntity<List<ProductDto>> entity = template.exchange("/products?name=carburetor", HttpMethod.GET, entity(), LIST_OF_PRODUCTS);
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
        ResponseEntity<ProductDto> entity = template.exchange("/products", POST, entity(product), ProductDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        ProductDto prod = entity.getBody();
        assertNotNull(prod);
        assertEquals("TestProduct", prod.getName());
    }

    @Test
    public void can_add_product_with_category() {
        Category category = new Category();
        category.setName("Category 1");
        categoryRepository.save(category);
        CategoryDto categoryDto = new CategoryDto(category);

        ProductDto product = new ProductDto();
        product.setName("TestProduct");
        product.setDescription("Random");
        product.setAmount(10);
        product.setPrice(5.0);
        product.setCategory(categoryDto);

        ResponseEntity<ProductDto> entity = template.exchange("/products", POST, entity(product), ProductDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        ProductDto prod = entity.getBody();
        assertNotNull(prod);
        assertEquals("TestProduct", prod.getName());
    }

    @Test
    public void can_update_product() {
        ProductDto dummyProduct = new ProductDto();
        dummyProduct.setName("DummyItem");
        ResponseEntity<ProductDto> entity = template.exchange("/products", POST, entity(dummyProduct), ProductDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        long id = entity.getBody().getId();

        ProductDto product = new ProductDto();
        product.setName("Realname");
        entity = template.exchange(String.format("/products/%d", id), PUT, entity(product), ProductDto.class);
        ProductDto updatedProduct = entity.getBody();
        assertNotNull(updatedProduct);
        assertEquals("Realname", updatedProduct.getName());
        template.exchange(String.format("/products/%d", id), HttpMethod.DELETE, entity(), ProductDto.class);
    }

    private HttpEntity<ProductDto> entity(ProductDto productDto) {
        return new HttpEntity<>(productDto, authorizationHeader());
    }

    private HttpEntity<ProductDto> entity() {
        return new HttpEntity<>(authorizationHeader());
    }

    public HttpHeaders authorizationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtTokenProvider.createTokenForTests("user"));
        return headers;
    }
}
