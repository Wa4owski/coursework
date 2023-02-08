package databases.itmo.coursework.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "moderator")//, schema = "s312431", catalog = "studs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModeratorEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @OneToOne(targetEntity = PersonEntity.class)
    private PersonEntity person;

}
