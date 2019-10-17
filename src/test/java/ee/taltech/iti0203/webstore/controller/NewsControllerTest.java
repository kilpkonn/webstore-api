package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.pojo.NewsDto;
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
public class NewsControllerTest {

    @Autowired
    private TestRestTemplate template;

    private static final ParameterizedTypeReference<List<NewsDto>> LIST_OF_NEWS = new ParameterizedTypeReference<>() {
    };

    @Test
    public void application_returns_list_of_news() {
        ResponseEntity<List<NewsDto>> entity = template.exchange("/news", HttpMethod.GET, null, LIST_OF_NEWS);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        List<NewsDto> news = entity.getBody();
        assertTrue(isNotEmpty(news));
        assertTrue(news.stream().anyMatch(p -> p.getHeadline().equals("Imminent downtime.")));
    }

    @Test
    public void can_sort_news_by_date() {
        ResponseEntity<List<NewsDto>> entity = template.exchange("/news?sort=latest", HttpMethod.GET, null, LIST_OF_NEWS);
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
        HttpEntity<NewsDto> httpEntity = new HttpEntity<>(news);
        ResponseEntity<NewsDto> entity = template.exchange("/news", POST, httpEntity, NewsDto.class);
        assertEquals(HttpStatus.OK, entity.getStatusCode());
        NewsDto newsItem = entity.getBody();
        assertNotNull(newsItem);
        assertEquals("Head Line", newsItem.getHeadline());
    }
}
