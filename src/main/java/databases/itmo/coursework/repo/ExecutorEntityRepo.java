package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.CustomerEntity;
import databases.itmo.coursework.entities.ExecutorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExecutorEntityRepo extends JpaRepository<ExecutorEntity, Integer> {
    Optional<ExecutorEntity> findByPersonId(Integer integer);

}
