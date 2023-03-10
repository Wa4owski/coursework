package databases.itmo.coursework.entities;

import databases.itmo.coursework.entities.keys.OrderRequestExecutorId;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.model.OrderRequestStatus;
import databases.itmo.coursework.model.OrderVisibility;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "order_request")//, schema = "s312431", catalog = "studs")
public class OrderRequestEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;
    @ManyToOne(targetEntity = CustomerEntity.class)
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @Column(name = "created_at")
    @CreationTimestamp
    private Timestamp createdAt;

    @ManyToOne(targetEntity = CompetenceEntity.class)
    @JoinColumn(name = "competence")
    private CompetenceEntity competence;
    @Basic
    @Column(name = "price", nullable = false)
    private Integer price;
    @Basic
    @Column(name = "description", nullable = false)
    private String description;
    @Basic
    @Column(name = "isPrivate", nullable = true)
    private Boolean isPrivate;

    @Column(name = "status", nullable = true)
    @Enumerated(value = EnumType.STRING)
    private OrderRequestStatus status;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(targetEntity = OrderRequestExecutorEntity.class, mappedBy = "primaryKey.orderRequest",
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OrderRequestExecutorEntity> orderRequestExecutors = new HashSet<>();

    public void addExecutor(ExecutorEntity executor, Boolean customerAgr, Boolean executorAgr){
        OrderRequestExecutorId primaryKey = new OrderRequestExecutorId( this, executor);
        OrderRequestExecutorEntity orderRequestExecutor = new OrderRequestExecutorEntity(primaryKey, customerAgr, executorAgr);
        this.orderRequestExecutors.add(orderRequestExecutor);
    }

    public OrderRequestEntity(CustomerEntity customer, CompetenceEntity competence, OrderRequest orderRequest) {
        this.customer = customer;
        this.name = orderRequest.getShortName();
        this.competence = competence;
        this.price = orderRequest.getPrice();
        this.description = orderRequest.getDescription();
        this.isPrivate = orderRequest.getAccess().equals(OrderVisibility.private_);
        this.status = OrderRequestStatus.opened;
    }
}
