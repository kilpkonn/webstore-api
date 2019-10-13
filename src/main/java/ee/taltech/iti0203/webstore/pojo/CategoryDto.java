package ee.taltech.iti0203.webstore.pojo;

import ee.taltech.iti0203.webstore.model.Product;
import ee.taltech.iti0203.webstore.model.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryDto {

    private Long id;
    private String name;
    private Set<Product> products;

    public CategoryDto(Category category) {
        this.id = category.getId();
        this.name = category.getName();
        this.products = category.getProducts();
    }
}
