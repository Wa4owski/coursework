package databases.itmo.coursework.entities.keys;

import databases.itmo.coursework.entities.ExecutorEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import jakarta.persistence.CascadeType;
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
    @ManyToOne(targetEntity = OrderRequestEntity.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "order_request_id", nullable = false)
    private OrderRequestEntity orderRequest;

    @ManyToOne(targetEntity = ExecutorEntity.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "executor_id", nullable = false)
    private ExecutorEntity executor;
}
