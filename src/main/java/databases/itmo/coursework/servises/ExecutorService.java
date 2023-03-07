package databases.itmo.coursework.servises;

import databases.itmo.coursework.entities.CompetenceEntity;
import databases.itmo.coursework.entities.ExecutorEntity;
import databases.itmo.coursework.entities.OrderRequestEntity;
import databases.itmo.coursework.entities.OrderRequestExecutorEntity;
import databases.itmo.coursework.entities.keys.OrderRequestExecutorId;
import databases.itmo.coursework.model.OrderRequest;
import databases.itmo.coursework.repo.ExecutorRepo;
import databases.itmo.coursework.repo.OrderRequestExecutorRepo;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExecutorService extends AbstractUserService{

    @Autowired
    ExecutorRepo executorRepo;

    @Autowired
    OrderRequestExecutorRepo orderRequestExecutorRepo;
    public List<OrderRequest> getOrderRequestsByCompetence(String competenceName){
//        if(competenceRepo.findByCompetence(competenceName).isEmpty()){
//            throw new ExecutionException("No competence with such name");
//        }
        return competenceRepo.findByCompetence(competenceName)
                .orElseThrow(() -> new ExecutionException("No competence with such name"))
                .getOrderRequests().stream()
                .filter(orderRequest -> !orderRequest.getIsPrivate())
                .map(OrderRequest::new).collect(Collectors.toList());
//        return orderRequestRepo.findAllByCompetence(competenceName).stream().
//                filter(orderRequest -> !orderRequest.getIsPrivate())
//                .map(OrderRequest::new).collect(Collectors.toList());
    }


    @Transactional
    public void chooseOrderRequest(OrderRequest orderRequest, Integer executorId){
        CompetenceEntity neededCompetence = competenceRepo.findByCompetence(orderRequest.getCompetence())
                .orElseThrow(() -> new ExecutionException("Incorrect competence name"));
        ExecutorEntity executor = executorRepo.findById(executorId)
                .orElseThrow(() -> new ExecutionException("No executor with such id"));
        if(!executor.getCompetences().contains(neededCompetence)){
            throw new ExecutionException("Executor hasn't needed competence");
        }
        OrderRequestEntity orderRequestEntity = orderRequestRepo.findById(orderRequest.getId())
                .orElseThrow(() -> new ExecutionException("No orderRequest with such id"));

        OrderRequestExecutorId primaryKey = new OrderRequestExecutorId(orderRequestEntity, executor);
        OrderRequestExecutorEntity orderRequestExecutor = orderRequestExecutorRepo
                .findByPrimaryKey(primaryKey).orElse(null);
        if(orderRequestExecutor != null){
            if(orderRequestExecutor.getExecutorAgr()){
                throw new ExecutionException("executor has already given agr to this orderRequest");
            }
            orderRequestExecutor.setExecutorAgr(true);
            orderRequestExecutorRepo.save(orderRequestExecutor);
        }
        else{
            orderRequestEntity.addExecutor(executor, null, true);
            //System.out.println(orderRequestExecutorRepo.findByPrimaryKey(primaryKey));
            //orderRequestExecutorRepo.save();
            //System.out.println(orderRequestExecutorRepo.findByPrimaryKey(primaryKey));
            orderRequestRepo.save(orderRequestEntity);
           // System.out.println(orderRequestExecutorRepo.findByPrimaryKey(primaryKey));

        }

    }
}
