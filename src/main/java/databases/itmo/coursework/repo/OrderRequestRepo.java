package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.OrderRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRequestRepo extends JpaRepository<OrderRequestEntity, Integer> {

    List<OrderRequestEntity> findAllByCustomerId(Integer customerId);
}
