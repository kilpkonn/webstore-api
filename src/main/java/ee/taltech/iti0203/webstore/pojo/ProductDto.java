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
    private String imageUrl;
    private Integer amount;
    private Double priceLow;
    private Double priceHigh;
    private CategoryDto category;
}
