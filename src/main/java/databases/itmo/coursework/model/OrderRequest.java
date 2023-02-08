package databases.itmo.coursework.model;

import databases.itmo.coursework.entities.CompetenceEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import databases.itmo.coursework.repo.CompetenceRepo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderRequest {

    @NotBlank(message = "Имя заказа не может быть пустым")
    @Length(max = 100, message = "Введенное имя слишком длинное")
    String shortName;
    @NotBlank(message = "Описание заказа не может быть пустым")
    @Length(min = 100, message = "Введенное описание слишком короткое")
    String description;
    @Positive(message = "Цена должна быть положительной")
    int price;
    @NotBlank(message = "Выберите требуемую от исполнителя компетенцию")
    String competence;
    @NotNull(message = "Выберите один из вариантов")
    @Enumerated(value = EnumType.STRING)
    OrderVisibility access;

    public OrderRequest(OrderRequestEntity orderRequest) {
        this.shortName = orderRequest.getName();
        this.description = orderRequest.getDescription();
        this.price = orderRequest.getPrice();
        this.competence = orderRequest.getCompetence().getCompetence();
    }

    //    private CompetenceEntity competenceEntity;
//    public void setCompetence(String competence) {
//        this.competenceEntity = competenceRepo.findByCompetence(competence)
//                .orElseThrow(() -> new RuntimeException("competence is out of database"));
//        this.competence = competence;
//    }
}
