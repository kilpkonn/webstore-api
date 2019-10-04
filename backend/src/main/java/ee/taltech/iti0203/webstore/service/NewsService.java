package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.NewsNotFoundException;
import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.pojo.NewsDto;
import ee.taltech.iti0203.webstore.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public News getById(Long id) {
        return newsRepository.findById(id)
                .orElseThrow(NewsNotFoundException::new);
    }

    public NewsDto createNewNews(NewsDto newsDto) {
        News news = new News(newsDto);
        return new NewsDto(newsRepository.save(news));
    }

    public NewsDto updateExistingNews(NewsDto newsDto, Long id) {
        News newNews = new News(newsDto);
        return newsRepository.findById(id)
                .map(news -> {
                    news.setHeadline(newNews.getHeadline());
                    news.setContent(newNews.getContent());
                    return new NewsDto(newsRepository.save(news));
                }).orElseThrow(NewsNotFoundException::new);
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

}
