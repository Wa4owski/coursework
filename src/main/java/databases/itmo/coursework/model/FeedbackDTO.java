package databases.itmo.coursework.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

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
}
