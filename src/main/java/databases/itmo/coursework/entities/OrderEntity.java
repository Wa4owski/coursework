package databases.itmo.coursework.entities;


import databases.itmo.coursework.entities.keys.FeedbackId;
import databases.itmo.coursework.model.FeedbackDTO;
import databases.itmo.coursework.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_", schema = "s312431")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_request_id", nullable = false)
    OrderRequestEntity orderRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id", nullable = false)
    ExecutorEntity executor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    CustomerEntity customer;

    @Column(name = "status", columnDefinition = "varchar(10) default 'started'", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;


    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "feedbackId.order", fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    List<FeedbackEntity> feedbacks = new ArrayList<>();

    public boolean clientSentFeedback(FeedbackId.ClientType clientType){
        return feedbacks.stream().anyMatch(feedbackEntity ->
                feedbackEntity.getFeedbackId().getAuthor()
                        .equals(clientType));
    }

    public void addFeedback(FeedbackDTO feedback, FeedbackId.ClientType clientType){
        FeedbackId feedbackId = new FeedbackId(this, clientType);
        FeedbackEntity feedbackEntity = new FeedbackEntity(feedbackId, feedback);
        this.feedbacks.add(feedbackEntity);
    }
}
