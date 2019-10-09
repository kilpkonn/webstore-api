package ee.taltech.iti0203.webstore.model;

import ee.taltech.iti0203.webstore.pojo.NewsDto;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import java.util.Date;

@Data
@Entity
public class News {

    @Id
    @GeneratedValue
    private long id;
    private String headline;
    private String content;
    private Date createdAt;

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            createdAt = new Date();
        }
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
