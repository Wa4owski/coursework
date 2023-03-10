package databases.itmo.coursework.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback")
public class FeedbackEntity {

    public enum ClientType {customer, executor};

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Id
    @Column(name = "author")
    @Enumerated(EnumType.STRING)
    private ClientType author;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "feedback")
    private String feedback;

    @Column(name = "author_wants_ticket", columnDefinition = "boolean default false")
    private Boolean authorWantsTicket;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp")
    private Timestamp createdAt;
}