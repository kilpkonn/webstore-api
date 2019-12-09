package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.repository.CategoryRepository;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImageControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;  // Needed to test product categories

    @Before
    public void setUp() {
        // Just in case
        productRepository.deleteAll();
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
        productRepository.saveAll(Arrays.asList(p1, p2, p3, p4, p5, p6, p7));
    }

    @After
    public void cleanUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Ignore
    public void testUpload() throws IOException {
        FileInputStream img = new FileInputStream(Paths.get("./images/placeholder.jpg").toFile());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        var entity = new HttpEntity<>(img, headers);
        ResponseEntity<String> resp = template.exchange("/upload/image", HttpMethod.POST, entity, String.class);
        assertNotNull(resp.getBody());
    }
}
