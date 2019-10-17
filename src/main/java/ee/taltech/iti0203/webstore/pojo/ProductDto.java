package ee.taltech.iti0203.webstore.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ProductDto {

    private Long id;
    private String name;
    private String description;
    private Integer amount;
    private Double price;
    private CategoryDto category;
}
