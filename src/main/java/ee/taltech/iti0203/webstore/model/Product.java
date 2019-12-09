package ee.taltech.iti0203.webstore.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ee.taltech.iti0203.webstore.pojo.ProductDto;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Entity
@Table(schema = "webstore")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer amount;
    private Double price;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "category")
    @JsonIgnoreProperties("products")
    private Category category;

    public Product() {
    }

    public Product(String name, String description, Integer amount, Double price) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.price = price;
    }

    public Product(ProductDto productDto) {
        this.name = productDto.getName();
        this.description = productDto.getDescription();
        this.imageUrl = productDto.getImageUrl() != null ? productDto.getImageUrl() : "/placeholder.jpg";
        this.amount = productDto.getAmount();
        this.price = productDto.getPrice();
        if (productDto.getCategory() != null) {
            this.category = new Category(productDto.getCategory());
        }
    }
}
