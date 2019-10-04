package ee.taltech.iti0203.webstore.model;

import ee.taltech.iti0203.webstore.pojo.ProductDto;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Product {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private int amount;

    public Product(String name, String description, int amount) {
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    public Product(ProductDto productDto) {
        this.name = productDto.getName();
        this.description = productDto.getDescription();
        this.amount = productDto.getAmount();
    }
}
