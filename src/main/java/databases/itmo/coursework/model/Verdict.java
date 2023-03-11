package databases.itmo.coursework.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Verdict {
    private Integer order_id;
    @Range(min=1, max=5)
    private Integer new_rate_for_executor;
    @Range(min=1, max=5)
    private Integer new_rate_for_customer;
    private Boolean ban_executor = false;
    private Boolean ban_customer = false;
    private Boolean delete_feedback_about_executor = false;
    private Boolean delete_feedback_about_customer = false;
}
