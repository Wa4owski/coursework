package databases.itmo.coursework.servises;

import databases.itmo.coursework.entities.CompetenceEntity;
import databases.itmo.coursework.entities.CustomerEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.repo.CompetenceRepo;
import databases.itmo.coursework.repo.CustomerRepo;
import databases.itmo.coursework.repo.OrderRequestRepo;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class CustomerService {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CompetenceRepo competenceRepo;

    @Autowired
    OrderRequestRepo orderRequestRepo;

    public List<String> getAllCompetences(){
        return competenceRepo.findAll().stream().map(CompetenceEntity::getCompetence).collect(Collectors.toList());
    }
    public void createNewOrderRequest(Integer customerId, OrderRequest orderRequest){
        CustomerEntity customer = customerRepo.findById(customerId)
                .orElseThrow(()->new ExecutionException("customerId is incorrect"));
        CompetenceEntity competence = competenceRepo.findByCompetence(orderRequest.getCompetence())
                .orElseThrow(()->new ExecutionException("competence is out of database"));
        orderRequestRepo.save(new OrderRequestEntity(customer, competence, orderRequest));
    }

    public List<OrderRequest> getOrderRequests(Integer customerId){
        return orderRequestRepo.findAllByCustomerId(customerId).stream()
                .map(OrderRequest::new).collect(Collectors.toList());
    }
}
