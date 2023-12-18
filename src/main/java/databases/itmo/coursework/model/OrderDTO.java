package databases.itmo.coursework.model;

import databases.itmo.coursework.entities.OrderEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class OrderDTO {

    private Integer id;
    private Integer executorId;
    private Integer customerId;
    private OrderRequest orderRequest;

    private OrderStatus status;

    private Executor executor;
    private Customer customer;

    public OrderDTO(OrderEntity orderEntity) {
        this.id = orderEntity.getId();
        this.executor = new Executor(orderEntity.getExecutor());
        this.executorId = executor.getId();
        this.customer = new Customer(orderEntity.getCustomer());
        this.customerId = customer.getId();
        this.orderRequest = new OrderRequest(orderEntity.getOrderRequest());
        this.status = orderEntity.getStatus();
    }
}
