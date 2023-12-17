package databases.itmo.coursework.servises;

import databases.itmo.coursework.entities.*;
import databases.itmo.coursework.entities.keys.FeedbackId;
import databases.itmo.coursework.entities.keys.OrderRequestExecutorId;
import databases.itmo.coursework.model.*;
import databases.itmo.coursework.repo.CustomerRepo;
import databases.itmo.coursework.repo.ExecutorRepo;
import databases.itmo.coursework.repo.OrderRequestExecutorRepo;
import lombok.AllArgsConstructor;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomerService extends AbstractUserService{

    private static final int maxPrivateExecutorsAmount = 3;

    private final CustomerRepo customerRepo;

    private final OrderRequestExecutorRepo orderRequestExecutorRepo;

    private final ExecutorRepo executorRepo;

    public Integer createNewOrderRequest(Integer customerId, OrderRequest orderRequest){
        CustomerEntity customer = customerRepo.findById(customerId)
                .orElseThrow(()->new ExecutionException("customerId is incorrect"));
        CompetenceEntity competence = competenceRepo.findByCompetence(orderRequest.getCompetence())
                .orElseThrow(()->new ExecutionException("competence is out of database"));
        return orderRequestRepo.save(new OrderRequestEntity(customer, competence, orderRequest)).getId();
    }

    public List<OrderRequest> getOpenedOrderRequests(Integer customerId){
        return orderRequestRepo.findAllByCustomerId(customerId)
                .stream()
                .filter(o -> o.getStatus().equals(OrderRequestStatus.opened))
                .map(OrderRequest::new)
                .collect(Collectors.toList());
    }


    public List<OrderDTO> getAllCustomerOrders(Integer customerId){
        return customerRepo.findById(customerId)
                .orElseThrow(() -> new ExecutionException("No customer with such id")).getOrders()
                .stream()
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getCustomerOrdersWithNoFeedback(Integer customerId){
        return customerRepo.findById(customerId)
                .orElseThrow(() -> new ExecutionException("No customer with such id"))
                .getOrders()
                .stream()
                .filter(o -> !o.clientSentFeedback(FeedbackId.ClientType.customer))
                .map(OrderDTO::new)
                .collect(Collectors.toList());
    }

    public List<Executor> getExecutorsByCompetence(String competenceName) {
        return competenceRepo.findByCompetence(competenceName)
                .orElseThrow(() -> new ExecutionException("No competence with such name"))
                .getExecutors()
                .stream()
                .map(Executor::new).collect(Collectors.toList());
    }

    public int  addExecutorToOrderRequest(OrderRequestExecutorIdDto orderRequestExecutorIdDto, Integer customerId) throws ExecutionException {
        OrderRequestEntity orderRequest = orderRequestRepo.findById(orderRequestExecutorIdDto.getOrderRequestId())
                .orElseThrow(()->new ExecutionException("No orderRequest with such id"));
        if(orderRequest.getCustomer().getId() != customerId){
            throw new ExecutionException("orderRequest doesn't belong to customer");
        }
        ExecutorEntity executor = executorRepo.findById(orderRequestExecutorIdDto.getExecutorId())
                .orElseThrow(()->new ExecutionException("No executor with such id"));

        OrderRequestExecutorId primaryKey = new OrderRequestExecutorId(orderRequest, executor);
        Optional<OrderRequestExecutorEntity> optionalOrderRequestExecutor = orderRequestExecutorRepo.findByPrimaryKey(primaryKey);
        OrderRequestExecutorEntity orderRequestExecutor;
        if(optionalOrderRequestExecutor.isPresent()){
            orderRequestExecutor = optionalOrderRequestExecutor.get();
            if(orderRequestExecutor.customerAgrIsSet() &&
                    orderRequestExecutor.getCustomerAgr()){
                throw new ExecutionException("Such pair: <executor, orderRequest> is already exists");
            }
            else {
                orderRequestExecutor.setCustomerAgr(true);
            }
        }
        else {
            if (privateExecutorsPlacesRemain(orderRequest) == 0) {
                throw new ExecutionException("Already max amount of private executors");
            }
            orderRequestExecutor =
                    new OrderRequestExecutorEntity(primaryKey,
                            orderRequest.getIsPrivate(),
                            null);
        }
        orderRequestExecutorRepo.save(orderRequestExecutor);
        return privateExecutorsPlacesRemain(orderRequest);
    }

    private int privateExecutorsPlacesRemain(OrderRequestEntity orderRequest){
        return maxPrivateExecutorsAmount - (int) orderRequestExecutorRepo.findAllByPrimaryKeyOrderRequest(orderRequest)
                .stream().filter(OrderRequestExecutorEntity::getCustomerAgr).count();
    }

    public int privateExecutorsPlacesRemain(Integer orderRequestId, Integer customerId){
        OrderRequestEntity orderRequest = orderRequestRepo.findById(orderRequestId)
                .orElseThrow(()->new ExecutionException("No orderRequest with such id"));
        if(orderRequest.getCustomer().getId() != customerId){
            throw new ExecutionException("orderRequest doesn't belong to customer");
        }
        return privateExecutorsPlacesRemain(orderRequest);
    }

    public void declineExecutor(OrderRequestExecutorIdDto orderRequestExecutorIdDto, Integer customerId){

        OrderRequestExecutorEntity orderRequestExecutor = getOrderRequestExecutorEntityByIds(
                orderRequestExecutorIdDto.getOrderRequestId(),
                customerId,
                orderRequestExecutorIdDto.getExecutorId());

        if(!orderRequestExecutor.getCustomerAgr()){
            throw new ExecutionException("executor is already declined!");
        }

        orderRequestExecutor.setCustomerAgr(false);
        orderRequestExecutorRepo.save(orderRequestExecutor);
    }

    public void acceptExecutor(OrderRequestExecutorIdDto orderRequestExecutorIdDto, Integer customerId){
        OrderRequestExecutorEntity orderRequestExecutor = getOrderRequestExecutorEntityByIds(
                orderRequestExecutorIdDto.getOrderRequestId(),
                customerId,
                orderRequestExecutorIdDto.getExecutorId());

        try {
            if (orderRequestExecutor.getCustomerAgr()) {
                throw new ExecutionException("executor is already accepted!");
            }
        }
        catch (NullPointerException ignored){
            orderRequestExecutor.setCustomerAgr(true);
            orderRequestExecutorRepo.save(orderRequestExecutor);
        }
    }

    private OrderRequestExecutorEntity getOrderRequestExecutorEntityByIds(Integer orderRequestId,
                                               Integer customerId,
                                               Integer executorId){
        OrderRequestEntity orderRequest = orderRequestRepo.findById(orderRequestId)
                .orElseThrow(()->new ExecutionException("No orderRequest with such id"));
        if(orderRequest.getCustomer().getId() != customerId){
            throw new ExecutionException("orderRequest doesn't belong to customer");
        }
        ExecutorEntity executor = executorRepo.findById(executorId)
                .orElseThrow(()->new ExecutionException("No executor with such id"));

        OrderRequestExecutorId primaryKey = new OrderRequestExecutorId(orderRequest, executor);

        return orderRequestExecutorRepo.findByPrimaryKey(primaryKey)
                .orElseThrow(()->new ExecutionException("no such orderRequestExecutor"));
    }

    public void sendFeedback(FeedbackDTO feedback, Integer customerId) {
        OrderEntity order = orderRepo.findById(feedback.getOrderId())
                .orElseThrow(()->new ExecutionException("no order with such id"));
        if(order.getCustomer().getId() != customerId){
            throw new ExecutionException("this order doesn't belong to customer");
        }
        if(order.clientSentFeedback(FeedbackId.ClientType.customer)){
            throw new ExecutionException("feedback from customer was already sent");
        }

        order.addFeedback(feedback, FeedbackId.ClientType.customer);
        orderRepo.save(order);
    }

    public List<Executor> getFreeExecutorsByCompetence(Integer orderRequestId) {
        OrderRequestEntity orderRequest = orderRequestRepo.findById(orderRequestId)
                .orElseThrow(()->new IllegalArgumentException("no orderRequest with such id"));
        return executorRepo.findFreeExecutorsByCompetence(orderRequest.getId(), orderRequest.getCompetence())
                .stream().map(Executor::new).collect(Collectors.toList());
    }

    private ExecutorEntity getExecutorEntityById(Integer executorId) {
        return executorRepo.findById(executorId)
                .orElseThrow(()->new IllegalArgumentException("no executor with such id"));
    }

    public Executor getExecutorById(Integer executorId){
        return new Executor(getExecutorEntityById(executorId));
    }

    public List<FeedbackDTO> getAllExecutorsFeedbacks(Integer executorId){
        var executor = getExecutorEntityById(executorId);
        return orderRepo.findAllFeedbackAboutExecutor(executor)
                .stream().map(FeedbackDTO::new).collect(Collectors.toList());
    }
}
