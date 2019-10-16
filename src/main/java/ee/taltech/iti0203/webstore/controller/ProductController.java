package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.pojo.ProductDto;
import ee.taltech.iti0203.webstore.service.ProductService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

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

    @GetMapping("{id}/image")
    public ResponseEntity<InputStreamResource> getImage(@PathVariable Long id) throws IOException {
        String uri = "image/" + productService.getById(id).getName().toLowerCase().replace(" ", "_") + ".jpg";
        ClassPathResource imgFile = new ClassPathResource(uri);
        if (!imgFile.exists()) {
            imgFile = new ClassPathResource("image/placeholder.jpg");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(imgFile.getInputStream()));
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
