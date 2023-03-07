package databases.itmo.coursework.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Value;

@NoArgsConstructor
@Getter
@Setter
public class FeedbackDTO {
    @NotNull
    private Float rateForExecutor;
    @Length(min = 100, message = "Введенный отзыв слишком короткий")
    @Length(max = 255, message = "Введенный отзыв слишком длинный")
    private String feedbackAboutExecutor;
    @NotNull(message = "Выберите один из вариантов")
    private Boolean customerWantsTicket;
}
