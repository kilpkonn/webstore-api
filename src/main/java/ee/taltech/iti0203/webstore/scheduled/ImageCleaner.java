package ee.taltech.iti0203.webstore.scheduled;

import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.repository.NewsRepository;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ImageCleaner {

    private static final Logger LOG = LoggerFactory.getLogger(ImageCleaner.class);

    private final NewsRepository newsRepository;
    private final ProductRepository productRepository;

    public ImageCleaner(final NewsRepository newsRepository, final ProductRepository productRepository) {
        this.newsRepository = newsRepository;
        this.productRepository = productRepository;
    }

    @Scheduled(cron = "0 0 3 1/1 * ? *")
    public void create() {
        LOG.debug("Starting to clean hanging images...");

        List<String> usedImages = Stream.concat(
                newsRepository.findAll().stream().map(News::getImageUrl),
                productRepository.findAll().stream().map(Product::getImageUrl))
                .distinct()
                .collect(Collectors.toList());

        try (Stream<Path> paths = Files.walk(Paths.get("./images/"))) {
            paths.filter(Files::isRegularFile)
                    .filter(f -> !usedImages.contains(f.toString()))
                    .peek(p -> LOG.debug(p.toString()))
                    .forEach(f -> f.toFile().delete());
        } catch (IOException e) {
            e.printStackTrace();
            LOG.error(e.getMessage());
        }
        LOG.debug("Cleaning images finished!");
    }

}
