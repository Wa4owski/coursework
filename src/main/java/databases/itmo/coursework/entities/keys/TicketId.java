package databases.itmo.coursework.entities.keys;

import databases.itmo.coursework.entities.OrderEntity;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Embeddable
public class TicketId implements Serializable {
    @OneToOne(targetEntity = OrderEntity.class)
    @JoinColumn(name="id", nullable = false)
    private OrderEntity orderEntity;
}
