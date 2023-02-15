package databases.itmo.coursework.model;

import databases.itmo.coursework.entities.OrderRequestEntity;
import databases.itmo.coursework.entities.OrderRequestExecutorEntity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderRequest {

    private Integer id;
    @NotBlank(message = "Имя заказа не может быть пустым")
    @Length(max = 100, message = "Введенное имя слишком длинное")
    private String shortName;
    @NotBlank(message = "Описание заказа не может быть пустым")
    @Length(min = 100, message = "Введенное описание слишком короткое")
    private String description;
    @Positive(message = "Цена должна быть положительной")
    int price;
    @NotBlank(message = "Выберите требуемую от исполнителя компетенцию")
    private String competence;
    @NotNull(message = "Выберите один из вариантов")
    @Enumerated(value = EnumType.STRING)
    private OrderVisibility access;

    private Set<Executor> executors;

    @Getter
    private static class Executor{
        Integer id;
        String name;
        Float rate;
        Boolean customerAgr;
        Boolean executorAgr;

        private Executor(OrderRequestExecutorEntity orderRequestExecutor) {
            this.id = orderRequestExecutor.getPrimaryKey().getExecutor().getId();
            this.name = orderRequestExecutor.getPrimaryKey().getExecutor().getPerson().getFullName();
            this.rate = orderRequestExecutor.getPrimaryKey().getExecutor().getRate();
            this.customerAgr = orderRequestExecutor.getCustomerAgr();
            this.executorAgr = orderRequestExecutor.getExecutorAgr();
        }
    }

    public OrderRequest(OrderRequestEntity orderRequest) {
        this.shortName = orderRequest.getName();
        this.description = orderRequest.getDescription();
        this.price = orderRequest.getPrice();
        this.competence = orderRequest.getCompetence().getCompetence();
        this.executors = orderRequest.getOrderRequestExecutors()
                .stream().map(Executor::new).collect(Collectors.toSet());
        this.access = orderRequest.getIsPrivate() ? OrderVisibility.private_ : OrderVisibility.public_;
    }

}
