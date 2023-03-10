package databases.itmo.coursework.entities;

import databases.itmo.coursework.entities.keys.FeedbackId;
import databases.itmo.coursework.model.FeedbackDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback")
public class FeedbackEntity {

    @EmbeddedId
    FeedbackId feedbackId;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "author_wants_ticket", columnDefinition = "boolean default false")
    private Boolean authorWantsTicket;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    public FeedbackEntity(FeedbackId feedbackId, FeedbackDTO feedback) {
        this.feedbackId = feedbackId;
        this.rate = feedback.getRate();
        this.feedback = feedback.getFeedbackMessage();
        this.authorWantsTicket = feedback.getWantsTicket();
    }
}