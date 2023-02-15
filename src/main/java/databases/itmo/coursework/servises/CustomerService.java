package databases.itmo.coursework.servises;

import databases.itmo.coursework.entities.*;
import databases.itmo.coursework.entities.keys.OrderRequestExecutorId;
import databases.itmo.coursework.model.AddExecutorRequest;
import databases.itmo.coursework.model.Executor;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.repo.*;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final int maxPrivateExecutorsAmount = 3;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    CompetenceRepo competenceRepo;

    @Autowired
    OrderRequestRepo orderRequestRepo;

    @Autowired
    OrderRequestExecutorRepo orderRequestExecutorRepo;

    @Autowired
    ExecutorRepo executorRepo;

    public List<String> getAllCompetences(){
        return competenceRepo.findAll().stream().map(CompetenceEntity::getCompetence).collect(Collectors.toList());
    }
    public Integer createNewOrderRequest(Integer customerId, OrderRequest orderRequest){
        CustomerEntity customer = customerRepo.findById(customerId)
                .orElseThrow(()->new ExecutionException("customerId is incorrect"));
        CompetenceEntity competence = competenceRepo.findByCompetence(orderRequest.getCompetence())
                .orElseThrow(()->new ExecutionException("competence is out of database"));
        return orderRequestRepo.save(new OrderRequestEntity(customer, competence, orderRequest)).getId();
    }

    public List<OrderRequest> getOrderRequests(Integer customerId){
        return orderRequestRepo.findAllByCustomerId(customerId).stream()
                .map(OrderRequest::new).collect(Collectors.toList());
    }

    public List<Executor> getExecutorsByCompetence(String competenceName) {
        return competenceRepo.findByCompetence(competenceName)
                .orElseThrow(() -> new ExecutionException("No competence with such name"))
                .getExecutors().stream().map(Executor::new).collect(Collectors.toList());
    }

    public int addExecutorToOrderRequest(AddExecutorRequest addExecutorRequest) throws ExecutionException {
        OrderRequestEntity orderRequest = orderRequestRepo.findById(addExecutorRequest.getOrderRequestId())
                .orElseThrow(()->new ExecutionException("No orderRequest with such id"));
        ExecutorEntity executor = executorRepo.findById(addExecutorRequest.getExecutorId())
                .orElseThrow(()->new ExecutionException("No executor with such id"));

        OrderRequestExecutorId primaryKey = new OrderRequestExecutorId(orderRequest, executor);
        if(orderRequestExecutorRepo.findByPrimaryKey(primaryKey).isPresent()){
            throw new ExecutionException("Such pair: <executor, orderRequest> is already exists");
        }
        int privateExecutorsAmount = (int) orderRequestExecutorRepo.findAllByPrimaryKeyOrderRequest(orderRequest)
                .stream().filter(OrderRequestExecutorEntity::getCustomerAgr).count();
        if(privateExecutorsAmount == maxPrivateExecutorsAmount){
            throw new ExecutionException("Already max amount of private executors");
        }
        OrderRequestExecutorEntity orderRequestExecutor =
                new OrderRequestExecutorEntity(primaryKey,
                        orderRequest.getIsPrivate(),
                        false);
        orderRequestExecutorRepo.save(orderRequestExecutor);
        return maxPrivateExecutorsAmount - (++privateExecutorsAmount);
    }
}
