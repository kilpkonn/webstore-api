package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.service.ProductService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping
    public List<Product> products() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getById(id);
    }

    @PostMapping
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {
        return productService.createNewProduct(productDto);
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(@RequestBody ProductDto productDto, @PathVariable Long id) {
        return productService.updateExistingProduct(productDto, id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
