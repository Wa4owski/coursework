package databases.itmo.coursework.model;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class OrderRequestExecutorIdDto {
    private Integer executorId;
    private Integer orderRequestId;

    public OrderRequestExecutorIdDto(Integer orderRequestId, Integer executorId) {
        this.orderRequestId = orderRequestId;
        this.executorId = executorId;
    }
}
