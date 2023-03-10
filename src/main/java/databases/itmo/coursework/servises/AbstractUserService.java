package databases.itmo.coursework.servises;

import databases.itmo.coursework.entities.CompetenceEntity;
import databases.itmo.coursework.repo.CompetenceRepo;
import databases.itmo.coursework.repo.OrderRepo;
import databases.itmo.coursework.repo.OrderRequestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public abstract class AbstractUserService {
    @Autowired
    CompetenceRepo competenceRepo;

    @Autowired
    OrderRequestRepo orderRequestRepo;

    @Autowired
    OrderRepo orderRepo;

    public List<String> getAllCompetences(){
        return competenceRepo.findAll().stream().map(CompetenceEntity::getCompetence).collect(Collectors.toList());
    }
}
