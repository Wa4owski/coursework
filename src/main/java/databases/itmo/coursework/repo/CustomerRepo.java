package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity, Integer> {
    Optional<CustomerEntity> findByPersonId(Integer integer);
}
