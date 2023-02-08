package databases.itmo.coursework.entities;

import databases.itmo.coursework.model.UserRole;
import databases.itmo.coursework.model.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "customer", uniqueConstraints={
        @UniqueConstraint(name="uniqueAccount", columnNames={"person_id"})
})//, schema = "s312431", catalog = "studs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(targetEntity = PersonEntity.class)
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @Column(name = "rate", nullable = true, precision = 0)
    private Double rate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @OneToMany(mappedBy = "customer")
    List<OrderRequestEntity> orderRequests = new ArrayList<>();
}
