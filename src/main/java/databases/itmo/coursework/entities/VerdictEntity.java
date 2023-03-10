package databases.itmo.coursework.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verdict")
@IdClass(VerdictEntityId.class)
public class VerdictEntity {
    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private TicketEntity ticket;


    @Column(name = "new_rate_for_executor")
    private Float newRateForExecutor;

    @Column(name = "delete_feedback_about_executor", columnDefinition = "boolean default false")
    private Boolean deleteFeedbackAboutExecutor;

    @Column(name = "ban_executor", columnDefinition = "boolean default false")
    private Boolean banExecutor;

    @Column(name = "new_rate_for_customer")
    private Float newRateForCustomer;

    @Column(name = "delete_feedback_about_customer", columnDefinition = "boolean default false")
    private Boolean deleteFeedbackAboutCustomer;

    @Column(name = "ban_customer", columnDefinition = "boolean default false")
    private Boolean banCustomer;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class VerdictEntityId implements Serializable {
    private TicketEntity ticket;
}