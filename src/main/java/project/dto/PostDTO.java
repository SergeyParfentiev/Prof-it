package project.dto;

import javax.persistence.*;

@Entity
@Table(name = "post")
public class PostDTO {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private long userId;
    @Column
    private String title;
    @Column
    private String body;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
