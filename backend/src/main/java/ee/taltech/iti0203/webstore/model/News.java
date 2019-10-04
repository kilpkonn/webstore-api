package ee.taltech.iti0203.webstore.model;

import ee.taltech.iti0203.webstore.pojo.NewsDto;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class News {

    @Id
    @GeneratedValue
    private long id;
    private String headline;
    private String content;

    public News() {
    }

    public News(String headline, String content) {
        this.headline = headline;
        this.content = content;
    }

    public News(NewsDto newsDto) {
        this.headline = newsDto.getHeadline();
        this.content = newsDto.getContent();
    }
}
