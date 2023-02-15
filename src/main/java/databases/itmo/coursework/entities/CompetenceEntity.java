package databases.itmo.coursework.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "competence")
public class CompetenceEntity {

    @Id
    @Column(name = "competence", nullable = false)
    String competence;

    @OneToMany(mappedBy = "competence")
    List<OrderRequestEntity> orderRequests = new ArrayList<>();

    @Setter(AccessLevel.PRIVATE)
    @ManyToMany
    @JoinTable(
            name = "executor_competence",
            joinColumns = @JoinColumn(name = "competence"),
            inverseJoinColumns = @JoinColumn(name = "executor_id")
    )
    private List<ExecutorEntity> executors = new ArrayList<>();

}
