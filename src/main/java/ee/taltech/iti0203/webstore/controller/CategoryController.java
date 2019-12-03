package ee.taltech.iti0203.webstore.controller;

import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.security.Roles;
import ee.taltech.iti0203.webstore.service.CategoryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("categories")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> categories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public CategoryDto getCategory(@PathVariable Long id) {
        return categoryService.getById(id);
    }

    @Secured({Roles.ROLE_USER, Roles.ROLE_ADMIN})
    @PostMapping
    public CategoryDto saveCategory(@RequestBody CategoryDto categoryDto) {
        return categoryService.createNewCategory(categoryDto);
    }

    @Secured({Roles.ROLE_USER, Roles.ROLE_ADMIN})
    @PutMapping("/{id}")
    public CategoryDto updateProduct(@RequestBody CategoryDto categoryDto, @PathVariable Long id) {
        return categoryService.renameCategory(categoryDto, id);
    }

    @Secured({Roles.ROLE_USER, Roles.ROLE_ADMIN})
    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }

}
