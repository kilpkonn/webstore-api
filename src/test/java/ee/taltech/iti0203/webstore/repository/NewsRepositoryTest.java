package ee.taltech.iti0203.webstore.repository;

import ee.taltech.iti0203.webstore.model.News;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class NewsRepositoryTest {

    @Autowired
    NewsRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll();  // Just in case

        repository.save(new News("Headline 1", "Content 1"));
        repository.save(new News("Headline 2", "Content 2"));
        repository.save(new News("Headline 3", "Content 3"));
        repository.save(new News("Headline 4", "Content 4"));
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void getNewestFirst() {
        List<News> result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();
        assertEquals(4, result.size());
        assertEquals("Headline 4", result.get(0).getHeadline());
        assertEquals("Headline 3", result.get(1).getHeadline());
        assertEquals("Headline 2", result.get(2).getHeadline());
        assertEquals("Headline 1", result.get(3).getHeadline());
    }

    @Test
    public void getNewestFirstOrderedAfterRemove() {
        List<News> result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();

        assertEquals(4, result.size());
        repository.delete(result.get(0));
        repository.delete(result.get(2));

        result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();
        assertEquals(2, result.size());
        assertEquals("Headline 3", result.get(0).getHeadline());
        assertEquals("Headline 1", result.get(1).getHeadline());
    }

    @Test
    public void deleteNews() {
        List<News> result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();

        assertEquals(4, result.size());
        repository.delete(result.get(0));

        result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();
        assertEquals(3, result.size());

        result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();
        repository.delete(result.get(0));

        result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();
        assertEquals(2, result.size());
        repository.deleteAll();

        result = repository.findByCreatedAtNotNullOrderByCreatedAtDesc();
        assertEquals(0, result.size());
    }
}
