package net.atos.spring_webapp.model;

import lombok.Data;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;
    @Type(type = "text")
    @NotBlank
    private String message;
    @DateTimeFormat(pattern = "YYYY-mm-dd HH:MM:SS")
    private LocalDateTime addedDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reviewer;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(@NotBlank String message, User reviewer, Post post) {
        this.message = message;
        this.reviewer = reviewer;
        this.post = post;
    }
}
