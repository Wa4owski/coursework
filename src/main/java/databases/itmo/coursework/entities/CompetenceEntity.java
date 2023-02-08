package databases.itmo.coursework.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
