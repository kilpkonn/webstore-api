package ee.taltech.iti0203.webstore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ee.taltech.iti0203.webstore.pojo.CategoryDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(exclude="products")
@Entity
@Table(schema = "webstore")
public class Category {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    @Column(unique=true)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Product> products;

    public Category() {
    }

    public Category(String name, Product... products) {
        this.name = name;
        this.products = Stream.of(products).collect(Collectors.toSet());
        this.products.forEach(p -> p.setCategory(this));
    }

    public Category(CategoryDto categoryDto) {
        this.name = categoryDto.getName();
    }

}
