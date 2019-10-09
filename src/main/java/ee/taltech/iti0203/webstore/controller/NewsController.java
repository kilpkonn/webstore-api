package ee.taltech.iti0203.webstore.controller;

import antlr.StringUtils;
import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.pojo.NewsDto;
import ee.taltech.iti0203.webstore.service.NewsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @GetMapping
    public List<News> news(@RequestParam(required=false) String sort) {
        if (sort != null && sort.equals("latest")) {
            return newsService.getAllNews().stream()
                    .filter(n -> n.getCreatedAt() != null)
                    .sorted(Comparator.comparing(News::getCreatedAt).reversed())
                    .collect(Collectors.toList());
        } else {
            return newsService.getAllNews();
        }
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
