package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.POST;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAddProduct() {
        Product product = new Product("Product", "Description", 11, 11);
        HttpEntity<Product> httpEntity = new HttpEntity<>(product);
        ResponseEntity<ProductDto> entity = restTemplate.exchange("/products", POST, httpEntity, ProductDto.class);
        System.out.println(entity.toString());
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        ProductDto prod = entity.getBody();
        assertNotNull(prod);
        assertEquals("Product", prod.getName());
    }
/*
    @Test
    public void testGetProducts() {
        ResponseEntity<List<ProductDto>> entity = restTemplate
                .exchange("/products",
                        GET,
                        null,
                        new ParameterizedTypeReference<List<ProductDto>>(){});
        assertNotNull(entity);
        List<ProductDto> products = entity.getBody();
        assertTrue(isNotEmpty(products));
        assertEquals(3, products.size());
    }
*/
}
