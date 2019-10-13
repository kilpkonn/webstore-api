package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.service.CategoryService;
import ee.taltech.iti0203.webstore.service.ProductService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("products")
public class ProductController {

    @Resource
    private ProductService productService;

    @GetMapping
    public List<ProductDto> products(@RequestParam(required = false) String category,
                                     @RequestParam(required = false) String name) {

        if (StringUtils.isEmpty(category) && StringUtils.isEmpty(name)) {
            return productService.getAllProducts();
        } else if (StringUtils.isEmpty(category)) {
            return productService.getByName(name);
        } else if (StringUtils.isEmpty(name)) {
            return productService.getByCategory(category);
        }
        return productService.getByNameAndCategory(name, category);
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
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
