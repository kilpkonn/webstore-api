package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.NewsNotFoundException;
import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.pojo.NewsDto;
import ee.taltech.iti0203.webstore.repository.NewsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsService {

    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public List<NewsDto> getAllNews() {
        return newsRepository.findAll().stream().map(NewsDto::new).collect(Collectors.toList());
    }

    public List<NewsDto> getLatestNews() {
        return newsRepository.findByCreatedAtNotNullOrderByCreatedAtDesc().stream()
                .map(NewsDto::new).collect(Collectors.toList());
    }

    public NewsDto getById(Long id) {
        return new NewsDto(newsRepository.findById(id)
                .orElseThrow(NewsNotFoundException::new));
    }

    public NewsDto createNewNews(NewsDto newsDto) {
        News news = new News(newsDto);
        return new NewsDto(newsRepository.save(news));
    }

    public NewsDto updateExistingNews(NewsDto newsDto, Long id) {
        Optional<News> existing = newsRepository.findById(id);
        if (existing.isEmpty()) {
            throw new NewsNotFoundException();
        }
        News news = existing.get();
        News newNews = new News(newsDto);
        news.setContent(newNews.getContent());
        news.setHeadline(newNews.getHeadline());
        news.setImageUrl(newNews.getImageUrl());
        return new NewsDto(newsRepository.save(news));
    }

    public void deleteNews(Long id) {
        newsRepository.deleteById(id);
    }

}
