package ee.taltech.iti0203.webstore.repository;

import ee.taltech.iti0203.webstore.model.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository repository;

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
