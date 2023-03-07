package databases.itmo.coursework.model;

import databases.itmo.coursework.entities.OrderEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class OrderDTO {
    private Integer executorId;
    private Integer customerId;
    private OrderRequest orderRequest;

    private OrderStatus status;

    public OrderDTO(OrderEntity orderEntity) {
        this.executorId = orderEntity.getExecutor().getId();
        this.customerId = orderEntity.getCustomer().getId();
        this.orderRequest = new OrderRequest(orderEntity.getOrderRequest());
        this.status = orderEntity.getStatus();
    }
}
