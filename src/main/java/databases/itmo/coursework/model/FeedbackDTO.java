package databases.itmo.coursework.model;

import databases.itmo.coursework.entities.FeedbackEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.sql.Timestamp;

@NoArgsConstructor
@Getter
@Setter
public class FeedbackDTO {
    @NotNull
    private Integer orderId;
    @NotNull
    private Float rate;
    @Length(min = 50, message = "Введенный отзыв слишком короткий")
    @Length(max = 255, message = "Введенный отзыв слишком длинный")
    private String feedbackMessage;
    @NotNull(message = "Выберите один из вариантов")
    private Boolean wantsTicket;

    private Timestamp createdAt;

    private OrderDTO order;

    public FeedbackDTO(FeedbackEntity feedback) {
        this.order = new OrderDTO(feedback.getFeedbackId().getOrder());
        this.rate = feedback.getRate();
        this.feedbackMessage = feedback.getFeedback();
        this.wantsTicket = feedback.getAuthorWantsTicket();
        this.createdAt = feedback.getCreatedAt();
    }
}
