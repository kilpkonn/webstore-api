package ee.taltech.iti0203.webstore.repository;

import ee.taltech.iti0203.webstore.model.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findByCreatedAtNotNullOrderByCreatedAtDesc();
}
