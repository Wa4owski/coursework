package databases.itmo.coursework.model;

import lombok.*;
import org.hibernate.validator.constraints.Range;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Verdict {
    private Integer orderId;
    @Range(min=1, max=5)
    private Integer newRateForExecutor;
    @Range(min=1, max=5)
    private Integer newRateForCustomer;
    private Boolean banExecutor = false;
    private Boolean banCustomer = false;
    private Boolean deleteFeedbackAboutExecutor = false;
    private Boolean deleteFeedbackAboutCustomer = false;
}
