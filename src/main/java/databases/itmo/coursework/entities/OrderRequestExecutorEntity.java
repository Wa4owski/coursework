package databases.itmo.coursework.entities;

import databases.itmo.coursework.entities.keys.OrderRequestExecutorId;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "order_request_executor")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequestExecutorEntity {
    @EmbeddedId
    private OrderRequestExecutorId primaryKey;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(name = "customer_agr")
    @Nullable
    private Boolean customerAgr;
    @Column(name = "executor_agr")
    @Nullable
    private Boolean executorAgr;

    public OrderRequestExecutorEntity(OrderRequestExecutorId primaryKey, Boolean customerAgr, Boolean executorAgr) {
        this.primaryKey = primaryKey;
        this.customerAgr = customerAgr;
        this.executorAgr = executorAgr;
    }
}
