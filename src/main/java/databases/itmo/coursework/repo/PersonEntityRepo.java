package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonEntityRepo extends JpaRepository<PersonEntity, Integer> {
    Optional<PersonEntity> findByEmail(String email);
}
