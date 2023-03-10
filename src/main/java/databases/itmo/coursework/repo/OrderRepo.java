package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepo extends JpaRepository<OrderEntity, Integer> {

    @Override
    Optional<OrderEntity> findById(Integer integer);
}
