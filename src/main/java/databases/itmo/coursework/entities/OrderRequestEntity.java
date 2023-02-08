package databases.itmo.coursework.entities;

import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.model.OrderRequestStatus;
import databases.itmo.coursework.model.OrderVisibility;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Objects;

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
    @Basic
    @Column(name = "created_at")
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
    @Column(name = "customer_default_agr", nullable = true)
    private Boolean customerDefaultAgr;
    @Basic
    @Column(name = "status", nullable = true)
    @Enumerated(value = EnumType.STRING)
    private OrderRequestStatus status;

    public OrderRequestEntity(CustomerEntity customer, CompetenceEntity competence, OrderRequest orderRequest) {
        this.customer = customer;
        this.name = orderRequest.getShortName();
        this.competence = competence;
        this.price = orderRequest.getPrice();
        this.description = orderRequest.getDescription();
        this.customerDefaultAgr = orderRequest.getAccess().equals(OrderVisibility.private_);
    }
}
