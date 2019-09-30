package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.NewsModel;
import ee.taltech.iti0203.webstore.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping
public class IndexController {

    @Autowired
    NewsRepository newsRepository;

    @GetMapping("/test")
    public List<NewsModel> test() {
        newsRepository.save(new NewsModel());
        NewsModel m = new NewsModel();
        m.setHeadline("testHeadline");
        newsRepository.save(m);
        return newsRepository.findAll();
    }
}
