package ee.taltech.iti0203.webstore.pojo;

import ee.taltech.iti0203.webstore.model.News;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ImageDto {


    private String url;

    public ImageDto(String url) {
        this.url = url;
    }
}
