package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.OrderEntity;
import databases.itmo.coursework.entities.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepo extends JpaRepository<TicketEntity, OrderEntity> {

}
