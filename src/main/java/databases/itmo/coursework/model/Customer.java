package databases.itmo.coursework.model;

import databases.itmo.coursework.entities.CustomerEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    private Integer id;
    private String name;
    private Float rate;

    public Customer(CustomerEntity customer) {
        this.id = customer.getId();
        this.name = customer.getPerson().getFullName();
        this.rate = customer.getRate();
    }
}
