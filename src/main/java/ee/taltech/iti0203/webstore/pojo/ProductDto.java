package ee.taltech.iti0203.webstore.pojo;

import ee.taltech.iti0203.webstore.model.Category;
import ee.taltech.iti0203.webstore.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private int amount;
    private Category category;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.amount = product.getAmount();
    }
}
