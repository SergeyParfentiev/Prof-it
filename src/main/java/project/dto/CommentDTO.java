package project.dto;

import javax.persistence.*;

@Entity
@Table(name = "comment")
public class CommentDTO {

    @Id
    @GeneratedValue
    private long id;
    @Column
    private long postId;
    @Column
    private String name;
    @Column
    private String email;
    @Column(length = 500)
    private String body;

    public CommentDTO() {
    }

    public CommentDTO(long postId, String name, String email, String body) {
        this.postId = postId;
        this.name = name;
        this.email = email;
        this.body = body;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
