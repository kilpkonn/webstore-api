package ee.taltech.iti0203.webstore.repository;

import ee.taltech.iti0203.webstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory_NameContainingIgnoreCase(String category);
    List<Product> findByNameContainingIgnoreCaseAndCategory_NameContainingIgnoreCase(String name, String category);
}
