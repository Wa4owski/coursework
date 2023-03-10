package databases.itmo.coursework.entities.keys;

import databases.itmo.coursework.entities.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class FeedbackId implements Serializable {

    public enum ClientType {customer, executor};

    @ManyToOne(fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "author")
    @Enumerated(EnumType.STRING)
    private ClientType author;
}
