package databases.itmo.coursework.entities;


import databases.itmo.coursework.model.OrderRequestStatus;
import databases.itmo.coursework.model.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "order_")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderEntity {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @OneToOne(targetEntity = OrderRequestEntity.class)
    @JoinColumn(name = "order_request_id", nullable = false)
    OrderRequestEntity orderRequest;

    @ManyToOne(targetEntity = ExecutorEntity.class)
    @JoinColumn(name = "executor_id", nullable = false)
    ExecutorEntity executor;

    @ManyToOne(targetEntity = CustomerEntity.class)
    @JoinColumn(name = "customer_id", nullable = false)
    CustomerEntity customer;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;
}
