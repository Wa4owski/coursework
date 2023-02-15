package databases.itmo.coursework.model;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
public class AddExecutorRequest {
    private Integer executorId;
    private Integer orderRequestId;
    private String executorName;

    public AddExecutorRequest(Integer orderRequestId, Integer executorId) {
        this.orderRequestId = orderRequestId;
        this.executorId = executorId;
    }
}
