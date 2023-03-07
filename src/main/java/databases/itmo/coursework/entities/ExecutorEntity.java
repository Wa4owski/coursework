package databases.itmo.coursework.entities;

import databases.itmo.coursework.model.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "executor")//, schema = "s312431", catalog = "studs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExecutorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @OneToOne(targetEntity = PersonEntity.class)
    private PersonEntity person;
    @Column(name = "rate", nullable = true, precision = 0)
    private Float rate;
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserStatus status;

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(mappedBy = "executors")
    private List<CompetenceEntity> competences = new ArrayList<>();

    public void addCompetence(CompetenceEntity competence){
        competence.getExecutors().add(this);
        competences.add(competence);
    }

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "primaryKey.executor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OrderRequestExecutorEntity> myOrderRequests = new HashSet<>();

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "executor", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<OrderEntity> myOrders = new HashSet<>();
}
