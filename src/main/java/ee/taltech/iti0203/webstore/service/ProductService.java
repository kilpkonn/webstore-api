package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.ProductNotFoundException;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> getAllProducts() {
        return convert(productRepository.findAll());
    }

    public List<ProductDto> getByName(String name) {
        return convert(productRepository.findByNameContainingIgnoreCase(name));
    }

    public List<ProductDto> getByCategory(String category) {
        return convert(productRepository.findByCategory_NameContainingIgnoreCase(category));
    }

    public List<ProductDto> getByNameAndCategory(String name, String category) {
        return convert(productRepository
                .findByNameContainingIgnoreCaseAndCategory_NameContainingIgnoreCase(name, category));
    }

    public ProductDto getById(Long id) {
        return convert(productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new));
    }

    public ProductDto createNewProduct(ProductDto productDto) {
        Product product = new Product(productDto);
        return convert(productRepository.save(product));
    }

    public ProductDto updateExistingProduct(ProductDto productDto, Long id) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isEmpty()) {
            throw new ProductNotFoundException();
        }
        Product product = existing.get();
        Product newProduct = new Product(productDto);
        product.setName(newProduct.getName());
        product.setAmount(newProduct.getAmount());
        product.setDescription(newProduct.getDescription());
        product.setCategory(newProduct.getCategory());
        return convert(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    private ProductDto convert(Product product) {
        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setId(product.getId());
        dto.setDescription(product.getDescription());
        dto.setAmount(product.getAmount());
        dto.setPrice(product.getPrice());
        if (product.getCategory() != null) {
            dto.setCategory(new CategoryDto(product.getCategory()));
        }
        return dto;
    }

    private List<ProductDto> convert(List<Product> products) {
        return products.stream().map(this::convert).collect(Collectors.toList());
    }

}
