package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.CustomerEntity;
import databases.itmo.coursework.entities.ModeratorEntity;
import org.springframework.boot.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jmx.export.naming.IdentityNamingStrategy;

import java.util.Optional;

public interface ModeratorEntityRepo extends JpaRepository<ModeratorEntity, Integer> {
    Optional<ModeratorEntity> findByPersonId(Integer integer);
}
