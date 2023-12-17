package databases.itmo.coursework.entities;

import databases.itmo.coursework.entities.keys.OrderRequestExecutorId;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "order_request_executor", schema = "s312431")
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

    public boolean customerAgrIsSet(){
        return this.customerAgr != null;
    }
    public boolean executorAgrIsSet(){
        return this.executorAgr != null;
    }

    public OrderRequestExecutorEntity(OrderRequestExecutorId primaryKey, Boolean customerAgr, Boolean executorAgr) {
        this.primaryKey = primaryKey;
        this.customerAgr = customerAgr;
        this.executorAgr = executorAgr;
    }
}
