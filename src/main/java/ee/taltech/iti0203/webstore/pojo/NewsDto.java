package ee.taltech.iti0203.webstore.pojo;

import ee.taltech.iti0203.webstore.model.News;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class NewsDto {

    private Long id;
    private String headline;
    private String content;
    private Date createdAt;

    public NewsDto(News news) {
        this.id = news.getId();
        this.headline = news.getHeadline();
        this.content = news.getContent();
        this.createdAt = news.getCreatedAt();
    }
}
