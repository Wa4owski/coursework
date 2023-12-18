package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.CompetenceEntity;
import databases.itmo.coursework.entities.ExecutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ExecutorRepo extends JpaRepository<ExecutorEntity, Integer> {
    Optional<ExecutorEntity> findByPersonId(Integer personId);

    @Query("select e from ExecutorEntity e where " +
            "(select count(o_r_e) from OrderRequestExecutorEntity o_r_e where o_r_e.primaryKey.executor = e" +
            " and o_r_e.primaryKey.orderRequest.id = :order_request_id) = 0 and :competence in " +
            "(select c from CompetenceEntity c)")
    List<ExecutorEntity> findFreeExecutorsByCompetence(@Param("order_request_id") Integer orderRequestId,
                                                       @Param("competence") CompetenceEntity competence);
}
