package databases.itmo.coursework.entities;

import databases.itmo.coursework.model.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "customer", schema = "s312431", uniqueConstraints={
        @UniqueConstraint(name="uniqueAccount", columnNames={"person_id"})
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "customer")
    List<OrderRequestEntity> orderRequests = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "customer")
    Set<OrderEntity> orders = new HashSet<>();
}
