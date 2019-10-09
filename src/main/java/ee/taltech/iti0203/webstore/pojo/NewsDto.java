package ee.taltech.iti0203.webstore.pojo;

import ee.taltech.iti0203.webstore.model.News;

import java.util.Date;

public class NewsDto {

    private Long id;
    private String headline;
    private String content;
    private Date createdAt;

    public NewsDto() {
    }

    public NewsDto(News news) {
        this.id = news.getId();
        this.headline = news.getHeadline();
        this.content = news.getContent();
        this.createdAt = news.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
