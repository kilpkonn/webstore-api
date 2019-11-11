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
    @EqualsAndHashCode.Exclude
    private Set<Product> products;

    public Category() {
    }

    public Category(CategoryDto categoryDto) {
        this.name = categoryDto.getName();
        // this.id = categoryDto.getId(); id should be generated in repo
    }

    public Category(String name) {
        this.name = name;
    }

}
