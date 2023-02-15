package databases.itmo.coursework.model;


import databases.itmo.coursework.entities.ExecutorEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Executor {
    private Integer id;
    private String name;
    private Float rate;

    public Executor(ExecutorEntity executorEntity) {
        this.id = executorEntity.getId();
        this.name = executorEntity.getPerson().getFullName();
        this.rate = executorEntity.getRate();
    }
}
