package databases.itmo.coursework.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "ticket")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TicketEntity {

    public enum TicketStatus {opened, closed};

    @Id
    @OneToOne(targetEntity = OrderEntity.class)
    @JoinColumn(name="id")
    private OrderEntity orderEntity;

    @Column(name = "moderator_id")
    private int moderatorId;

    @Column(name = "created_at", columnDefinition = "timestamp default current_timestamp")
    private Timestamp createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(10) default 'opened'")
    private TicketStatus status;
}