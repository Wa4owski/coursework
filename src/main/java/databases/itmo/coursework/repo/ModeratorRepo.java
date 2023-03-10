package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.ModeratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModeratorRepo extends JpaRepository<ModeratorEntity, Integer> {
    Optional<ModeratorEntity> findByPersonId(Integer integer);
}
