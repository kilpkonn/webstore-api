package ee.taltech.iti0203.webstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private Integer amount;
    private Double price;

    @ManyToOne
    @JoinColumn
    @JsonIgnoreProperties("products")
    private Category category;

    public Product() {
    }

    public Product(String name, String description, Integer amount, Integer price) {
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public Product(ProductDto productDto) {
        this.name = productDto.getName();
        this.description = productDto.getDescription();
        this.amount = productDto.getAmount();
        this.category = new Category(productDto.getCategory());
    }
}
