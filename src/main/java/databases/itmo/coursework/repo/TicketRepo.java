package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.OrderEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import databases.itmo.coursework.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;

public interface TicketRepo extends ListCrudRepository<TicketEntity, OrderEntity> {
    List<TicketEntity> findAll();
}
