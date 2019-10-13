package ee.taltech.iti0203.webstore.service;

import ee.taltech.iti0203.webstore.exception.CategoryNotFoundException;
import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import ee.taltech.iti0203.webstore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<CategoryDto> getAllCategories() {
        return repository.findAll().stream().map(CategoryDto::new).collect(Collectors.toList());
    }

    public CategoryDto getById(Long id) {
        return new CategoryDto(repository.findById(id)
                .orElseThrow(CategoryNotFoundException::new));
    }

    public CategoryDto createNewCategory(CategoryDto categoryDto) {
        Category category = new Category(categoryDto);
        return new CategoryDto(repository.save(category));
    }

    public CategoryDto renameCategory(CategoryDto categoryDto, Long id) {
        Category newCategory = new Category(categoryDto);
        return repository.findById(id)
                .map(category -> {
                    category.setName(newCategory.getName());
                    return new CategoryDto(repository.save(category));
                }).orElseThrow(CategoryNotFoundException::new);
    }

    public void deleteCategory(Long id) {
        repository.deleteById(id);
    }

}
