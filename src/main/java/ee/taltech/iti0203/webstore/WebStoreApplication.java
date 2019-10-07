package ee.taltech.iti0203.webstore;

import ee.taltech.iti0203.webstore.model.News;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.repository.NewsRepository;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class WebStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebStoreApplication.class, args);
	}

	@Bean
	public CommandLineRunner initProducts(ProductRepository repository) {
		return (args) -> {
			List<Product> products = List.of(
					new Product("Flower1", "Description1", 10),
					new Product("Flower2", "Description2", 20),
					new Product("Flower3", "Description3", 30),
					new Product("Flower4", "Description4", 40)
			);
			repository.saveAll(products);
		};
	}

	@Bean
	public CommandLineRunner initNews(NewsRepository repository) {
		return (args) -> {
			List<News> news = List.of(
					new News("Headline1", "Content1"),
					new News("Headline2", "Content2"),
					new News("Headline3", "Content3"),
					new News("Headline4", "Content4")
			);
			repository.saveAll(news);
		};
	}
}
