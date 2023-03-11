package databases.itmo.coursework.model;

import databases.itmo.coursework.entities.FeedbackEntity;
import databases.itmo.coursework.entities.OrderEntity;
import databases.itmo.coursework.entities.TicketEntity;
import databases.itmo.coursework.entities.keys.FeedbackId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Ticket {
    private OrderEntity orderEntity;
    private Integer moderatorId;

    private Float customerOrderRate;

    private Float executorOrderRate;

    private String customerFeedback;

    private String executorFeedback;

    private TicketEntity.TicketStatus status;

    public Ticket(TicketEntity ticketEntity) {
        this.moderatorId = ticketEntity.getModeratorId();
        this.orderEntity = ticketEntity.getTicketId().getOrderEntity();
        List<FeedbackEntity> q = this.orderEntity.getFeedbacks()
                .stream().filter(o -> o.getFeedbackId().getAuthor().equals(FeedbackId.ClientType.customer))
                .toList();
        if (q.size() > 0) {
            this.customerOrderRate = q.get(0).getRate();
            this.customerFeedback = q.get(0).getFeedback();
        }
        else {
            this.customerOrderRate = 4.0F;
            this.customerFeedback = "default";
        }
        q = this.orderEntity.getFeedbacks()
                .stream().filter(o -> o.getFeedbackId().getAuthor().equals(FeedbackId.ClientType.executor))
                .toList();
        if (q.size() > 0) {
            this.executorOrderRate = q.get(0).getRate();
            this.executorFeedback = q.get(0).getFeedback();
        }
        else {
            this.executorOrderRate = 4.0F;
            this.executorFeedback = "default";
        }
        this.status = ticketEntity.getStatus();
    }
}
