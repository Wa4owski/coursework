package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.OrderRequestEntity;
import databases.itmo.coursework.entities.OrderRequestExecutorEntity;
import databases.itmo.coursework.entities.keys.OrderRequestExecutorId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRequestExecutorRepo extends JpaRepository<OrderRequestExecutorEntity, OrderRequestExecutorId> {
    Optional<OrderRequestExecutorEntity> findByPrimaryKey(OrderRequestExecutorId primaryKey);

    List<OrderRequestExecutorEntity> findAllByPrimaryKeyOrderRequest(OrderRequestEntity orderRequest);
}
