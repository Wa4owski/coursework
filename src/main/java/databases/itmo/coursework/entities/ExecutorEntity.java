package databases.itmo.coursework.entities;

import databases.itmo.coursework.model.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "executor", schema = "s312431")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExecutorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    private PersonEntity person;
    @Column(name = "rate")
    private Float rate;
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "executors")
    private List<CompetenceEntity> competences = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "primaryKey.executor",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OrderRequestExecutorEntity> myOrderRequests = new HashSet<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "executor",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OrderEntity> myOrders = new HashSet<>();
}
