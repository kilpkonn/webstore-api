package ee.taltech.iti0203.webstore.model;

import ee.taltech.iti0203.webstore.pojo.NewsDto;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(schema = "webstore")
public class News {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    private String headline;
    private String content;
    private Date createdAt;

    @PrePersist
    private void prePersist() {
        createdAt = new Date();
    }

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
