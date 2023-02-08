package databases.itmo.coursework.entities;

import databases.itmo.coursework.model.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

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

}
