package databases.itmo.coursework.entities;

import databases.itmo.coursework.entities.keys.VerdictId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verdict", schema = "s312431")
public class VerdictEntity {
    @EmbeddedId
    private VerdictId verdictId;

    @Column(name = "new_rate_for_executor")
    private float newRateForExecutor;

    @Column(name = "delete_feedback_about_executor")
    private boolean deleteFeedbackAboutExecutor;

    @Column(name = "ban_executor")
    private boolean banExecutor;

    @Column(name = "new_rate_for_customer")
    private float newRateForCustomer;

    @Column(name = "delete_feedback_about_customer")
    private boolean deleteFeedbackAboutCustomer;

    @Column(name = "ban_customer")
    private boolean banCustomer;
}
