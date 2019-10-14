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

}
