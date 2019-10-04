package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.exception.ProductNotFoundException;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductRepository repository;

    public ProductController(ProductRepository productRepository) {
        this.repository = productRepository;
    }

    @GetMapping
    public List<Product> products() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return repository.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        return repository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setAmount(newProduct.getAmount());
                    product.setDescription(newProduct.getDescription());
                    return repository.save(product);
                }).orElseThrow(ProductNotFoundException::new);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
    }

}
