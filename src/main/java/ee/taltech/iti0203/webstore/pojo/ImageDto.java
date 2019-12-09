package ee.taltech.iti0203.webstore.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDto {


    private String url;

    public ImageDto(String url) {
        this.url = url;
    }
}
