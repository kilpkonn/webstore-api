package ee.taltech.iti0203.webstore.repository;

import ee.taltech.iti0203.webstore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
