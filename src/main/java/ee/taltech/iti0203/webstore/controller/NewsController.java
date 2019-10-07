package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.pojo.NewsDto;
import ee.taltech.iti0203.webstore.service.NewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @GetMapping
    public List<News> news() {
        return newsService.getAllNews();
    }

    @GetMapping("/{id}")
    public News getNews(@PathVariable Long id) {
        return newsService.getById(id);
    }

    @PostMapping
    public NewsDto saveNews(@RequestBody NewsDto newsDto) {
        return newsService.createNewNews(newsDto);
    }

    @PutMapping("/{id}")
    public NewsDto updateNews(@RequestBody NewsDto newsDto, @PathVariable Long id) {
        return newsService.updateExistingNews(newsDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Long id) {
        newsService.deleteNews(id);
    }
}
