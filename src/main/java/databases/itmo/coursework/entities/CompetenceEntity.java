package databases.itmo.coursework.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "competence", schema = "s312431")
public class CompetenceEntity {

    @Id
    @Column(name = "competence", nullable = false)
    String competence;

    @OneToMany(mappedBy = "competence")
    List<OrderRequestEntity> orderRequests = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "executor_competence",
            joinColumns = @JoinColumn(name = "competence"),
            inverseJoinColumns = @JoinColumn(name = "executor_id")
    )
    private Set<ExecutorEntity> executors = new HashSet<>();

}
