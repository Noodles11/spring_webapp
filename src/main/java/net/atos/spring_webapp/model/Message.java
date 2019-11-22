package net.atos.spring_webapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;
    private String title;
    @Type(type = "text")
    @NotBlank
    private String message;
    @DateTimeFormat(pattern = "YYYY-mm-dd HH:MM:SS")
    private LocalDateTime addedDate = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    public Message(String title, @NotBlank String message) {
        this.title = title;
        this.message = message;
    }
}
