package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.CompetenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompetenceRepo extends JpaRepository<CompetenceEntity, String> {

    Optional<CompetenceEntity> findByCompetence(String competenceName);
}
