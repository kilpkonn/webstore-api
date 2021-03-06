package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.ProductNotFoundException;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.repository.CategoryRepository;
import ee.taltech.iti0203.webstore.repository.ProductRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<ProductDto> getProducts(String name, String category) {
        if (StringUtils.isEmpty(category) && StringUtils.isEmpty(name)) {
            return convert(productRepository.findAll());
        } else if (StringUtils.isEmpty(category)) {
            return convert(productRepository.findByNameContainingIgnoreCase(name));
        } else if (StringUtils.isEmpty(name)) {
            return convert(productRepository.findByCategory_NameContainingIgnoreCase(category));
        }
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
        product.setName(productDto.getName());
        product.setAmount(productDto.getAmount());
        product.setPriceLow(productDto.getPriceLow());
        product.setPriceHigh(productDto.getPriceHigh());
        product.setDescription(productDto.getDescription());
        if (productDto.getCategory() != null) {
            product.setCategory(categoryRepository
                    .findById(productDto.getCategory()
                    .getId()
                    ).orElse(product.getCategory()));
        }
        return convert(productRepository.save(product));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public ResponseEntity<InputStreamResource> getProductImage(@PathVariable Long id) throws IOException {
        String uri = "image/" + getById(id).getName().toLowerCase().replace(" ", "_") + ".jpg";
        ClassPathResource imgFile = new ClassPathResource(uri);
        if (!imgFile.exists()) {
            imgFile = new ClassPathResource("image/placeholder.jpg");
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(imgFile.getInputStream()));
    }

    private ProductDto convert(Product product) {
        ProductDto dto = new ProductDto();
        dto.setName(product.getName());
        dto.setId(product.getId());
        dto.setDescription(product.getDescription());
        dto.setImageUrl(product.getImageUrl());
        dto.setAmount(product.getAmount());
        dto.setPriceLow(product.getPriceLow());
        dto.setPriceHigh(product.getPriceHigh());
        if (product.getCategory() != null) {
            dto.setCategory(new CategoryDto(product.getCategory()));
        }
        return dto;
    }

    private List<ProductDto> convert(List<Product> products) {
        return products.stream().map(this::convert).collect(Collectors.toList());
    }
}
