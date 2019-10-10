package ee.taltech.iti0203.webstore.pojo;

import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.model.Category;

import java.util.Set;

public class CategoryDto {

    private Long id;
    private String name;
    private Set<Product> products;

    public CategoryDto() {
    }

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.products = category.getProducts();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
