package databases.itmo.coursework.entities.keys;

import databases.itmo.coursework.entities.ExecutorEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import databases.itmo.coursework.model.Executor;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class OrderRequestExecutorId implements Serializable {
    @ManyToOne(targetEntity = OrderRequestEntity.class)
    @JoinColumn(name = "order_request_id", nullable = false)
    private OrderRequestEntity orderRequest;

    @ManyToOne(targetEntity = ExecutorEntity.class)
    @JoinColumn(name = "executor_id", nullable = false)
    private ExecutorEntity executor;
}
