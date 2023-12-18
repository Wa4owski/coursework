package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.CompetenceEntity;
import databases.itmo.coursework.entities.ExecutorEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRequestRepo extends JpaRepository<OrderRequestEntity, Integer> {

    List<OrderRequestEntity> findAllByCustomerId(Integer customerId);

    @Query("select o from OrderRequestEntity o where o.competence = :competence and " +
            "(select count(o_r_e) from OrderRequestExecutorEntity o_r_e where o_r_e.primaryKey.executor = :executor " +
            "and o_r_e.primaryKey.orderRequest = o) = 0")
    List<OrderRequestEntity> findFreeOrderRequestsForExecutorByCompetence(@Param("executor") ExecutorEntity executor, @Param("competence") CompetenceEntity competence);
}
