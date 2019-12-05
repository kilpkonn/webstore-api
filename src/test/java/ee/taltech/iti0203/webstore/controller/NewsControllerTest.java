package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.pojo.NewsDto;
import ee.taltech.iti0203.webstore.repository.NewsRepository;
import ee.taltech.iti0203.webstore.security.JwtTokenProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.junit.Assert.*;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class NewsControllerTest {

    @Autowired
    private TestRestTemplate template;

    @Resource
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private NewsRepository repository;

    private static final ParameterizedTypeReference<List<NewsDto>> LIST_OF_NEWS = new ParameterizedTypeReference<>() {
    };

    @Before
    public void setUp() {
        repository.deleteAll();
        News first = new News("Welcome to the flower store!", "Content");
        first.setCreatedAt(new GregorianCalendar(2019, Calendar.JUNE, 22).getTime());
        News second = new News("New flowers in stock!", "Content");
        second.setCreatedAt(new GregorianCalendar(2019, Calendar.JULY, 10).getTime());
        News third = new News("Imminent downtime.", "Content");
        third.setId(3L);
        third.setCreatedAt(new GregorianCalendar(2019, Calendar.AUGUST, 9).getTime());

        repository.save(first);
        repository.save(second);
        repository.save(third);
    }

    @After
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    public void application_returns_list_of_news() {
        ResponseEntity<List<NewsDto>> entity = template.exchange("/news", HttpMethod.GET, entity(), LIST_OF_NEWS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<NewsDto> news = entity.getBody();
        assertTrue(isNotEmpty(news));
        assertTrue(news.stream().anyMatch(p -> p.getHeadline().equals("Imminent downtime.")));
    }

    @Test
    public void can_sort_news_by_date() {
        ResponseEntity<List<NewsDto>> entity = template.exchange("/news?sort=latest", HttpMethod.GET, entity(), LIST_OF_NEWS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<NewsDto> news = entity.getBody();
        assertTrue(isNotEmpty(news));
        assertTrue(news.size() >= 2);
        assertTrue(news.get(0).getCreatedAt().after(news.get(news.size() - 1).getCreatedAt()));
    }

    @Test
    public void can_add_news() {
        NewsDto news = new NewsDto();
        news.setHeadline("Head Line");
        news.setContent("Con Tent");
        ResponseEntity<NewsDto> entity = template.exchange("/news", POST, entity(news), NewsDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        NewsDto newsItem = entity.getBody();
        assertNotNull(newsItem);
        assertEquals("Head Line", newsItem.getHeadline());
    }

    @Test
    public void can_get_news_by_id() {
        NewsDto dummy = new NewsDto();
        dummy.setHeadline("Just a test...");
        dummy.setContent("Nothing to worry about.");
        ResponseEntity<NewsDto> entity = template.exchange("/news", POST, entity(dummy), NewsDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        long id = entity.getBody().getId();

        ResponseEntity<NewsDto> response = template.exchange("/news/" + id, HttpMethod.GET, entity(), NewsDto.class);
        assertNotNull(response);
        NewsDto news = response.getBody();
        assertNotNull(news);
        assertEquals(dummy.getHeadline(), news.getHeadline());
    }

    @Test
    public void can_update_news_item() {
        NewsDto dummyNews = new NewsDto();
        dummyNews.setHeadline("Head Line");
        dummyNews.setContent("Con Tent");
        ResponseEntity<NewsDto> entity = template.exchange("/news", POST, entity(dummyNews), NewsDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        long id = entity.getBody().getId();

        NewsDto news = new NewsDto();
        news.setHeadline("New Headline!");
        news.setContent("Content");
        entity = template.exchange(String.format("/news/%d", id), PUT, entity(news), NewsDto.class);
        NewsDto newNews = entity.getBody();
        assertNotNull(newNews);
        assertEquals("New Headline!", newNews.getHeadline());
        template.exchange("/news/4", HttpMethod.DELETE, entity(), NewsDto.class);
    }

    @Test
    public void can_delete_news_by_id() {
        NewsDto dummyNews = new NewsDto();
        dummyNews.setHeadline("Head Line");
        dummyNews.setContent("Con Tent");
        ResponseEntity<NewsDto> entity = template.exchange("/news", POST, entity(dummyNews), NewsDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        long id = entity.getBody().getId();

        template.exchange(String.format("/news/%d", id), HttpMethod.DELETE, entity(), NewsDto.class);
        entity = template.exchange(String.format("/news/%d", id), HttpMethod.GET, entity(), NewsDto.class);
        assertNotNull(entity);
        assertNotNull(entity.getBody());
        assertNull(entity.getBody().getHeadline());
    }

    private HttpEntity<NewsDto> entity(NewsDto newsDto) {
        return new HttpEntity<>(newsDto, authorizationHeader());
    }

    private HttpEntity<NewsDto> entity() {
        return new HttpEntity<>(authorizationHeader());
    }

    public HttpHeaders authorizationHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtTokenProvider.createTokenForTests("user"));
        return headers;
    }
}
