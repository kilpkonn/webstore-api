package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.pojo.NewsDto;
import ee.taltech.iti0203.webstore.service.NewsService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("news")
public class NewsController {

    @Resource
    private NewsService newsService;

    @GetMapping
    public List<NewsDto> news(@RequestParam(required = false) String sort) {
        if (!StringUtils.isEmpty(sort) && sort.equals("latest")) {
            return newsService.getLatestNews();
        } else {
            return newsService.getAllNews();
        }
    }

    @GetMapping("/{id}")
    public NewsDto getNews(@PathVariable Long id) {
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
