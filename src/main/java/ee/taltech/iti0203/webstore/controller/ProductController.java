package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.service.ImageService;
import ee.taltech.iti0203.webstore.security.Roles;
import ee.taltech.iti0203.webstore.service.ProductService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("products")
public class ProductController {

    @Resource
    private ProductService productService;

    @Resource
    private ImageService imageService;

    @GetMapping
    public List<ProductDto> products(@RequestParam(required = false) String category,
                                     @RequestParam(required = false) String name) {
        return productService.getProducts(name, category);
    }

    @GetMapping("/{id}")
    public ProductDto getProduct(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Long id) throws IOException {
        return imageService.getImage(productService.getById(id).getImageUrl());
    }

    @Secured({Roles.ROLE_USER, Roles.ROLE_ADMIN})
    @PostMapping
    public ProductDto saveProduct(@RequestBody ProductDto productDto) {
        return productService.createNewProduct(productDto);
    }

    @Secured({Roles.ROLE_USER, Roles.ROLE_ADMIN})
    @PutMapping("/{id}")
    public ProductDto updateProduct(@RequestBody ProductDto productDto, @PathVariable Long id) {
        return productService.updateExistingProduct(productDto, id);
    }

    @Secured({Roles.ROLE_USER, Roles.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }

}
