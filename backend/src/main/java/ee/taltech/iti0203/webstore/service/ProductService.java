package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.ProductNotFoundException;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public ProductDto createNewProduct(ProductDto productDto) {
        Product product = new Product(productDto);
        return new ProductDto(productRepository.save(product));
    }

    public ProductDto updateExistingProduct(ProductDto productDto, Long id) {
        Product newProduct = new Product(productDto);
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setAmount(newProduct.getAmount());
                    product.setDescription(newProduct.getDescription());
                    return new ProductDto(productRepository.save(product));
                }).orElseThrow(ProductNotFoundException::new);
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
