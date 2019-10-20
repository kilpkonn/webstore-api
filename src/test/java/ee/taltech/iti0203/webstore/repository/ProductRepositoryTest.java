package ee.taltech.iti0203.webstore.repository;

import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

    @Autowired
    CategoryRepository categoryRepository;  // Needed to test product categories


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
    public void search_by_name() {
        List<Product> result = repository.findByNameContainingIgnoreCase("daf");
        assertTrue(isNotEmpty(result));
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Daffodil")));
    }

    @Test
    public void search_by_category_name() {
        List<Product> result = repository.findByCategory_NameContainingIgnoreCase("red");
        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(p -> p.getName().equals("Chrysanthemum")));
    }

    @Test
    public void search_by_name_and_category_name() {
        List<Product> result = repository
                .findByNameContainingIgnoreCaseAndCategory_NameContainingIgnoreCase("chrys", "red");
        assertEquals(1, result.size());
        assertEquals("Chrysanthemum", result.get(0).getName());
    }
}
