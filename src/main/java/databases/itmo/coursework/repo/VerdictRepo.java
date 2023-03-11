package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.OrderEntity;
import databases.itmo.coursework.entities.VerdictEntity;
import databases.itmo.coursework.model.Verdict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface VerdictRepo extends CrudRepository<VerdictEntity, OrderEntity> {
}
