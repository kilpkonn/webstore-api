package ee.taltech.iti0203.webstore;

import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.repository.NewsRepository;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebStoreApplicationTests {

    @Autowired
    NewsRepository newsRepository;

    @Autowired
    ProductRepository productRepository;

    @Before
    public void setUp() {
        List<News> news = List.of(
                new News("Headline1", "Content1"),
                new News("Headline2", "Content2"),
                new News("Headline3", "Content3"),
                new News("Headline4", "Content4")
        );
        newsRepository.saveAll(news);

        List<Product> products = List.of(
                new Product("Flower1", "Description1", 10),
                new Product("Flower2", "Description2", 20),
                new Product("Flower3", "Description3", 30),
                new Product("Flower4", "Description4", 40)
        );
        productRepository.saveAll(products);
    }

    @After
    public void cleanUp() {
        // Delete all test data
        newsRepository.deleteAll();
        productRepository.deleteAll();
    }

    @Test
    public void testFindAllNews() {
        // Get all the people
        List<News> news = newsRepository.findAll();

        assertEquals(4, news.size());
    }

    @Test
    public void testFindAllProducts() {
        List<Product> products = productRepository.findAll();

        assertEquals(4, products.size());
    }


}
