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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExecutorService extends AbstractUserService {

    private final ExecutorRepo executorRepo;

    private final CustomerRepo customerRepo;

    public List<OrderRequest> getFreeOrderRequestsByCompetence(String competenceName) {
        return competenceRepo.findByCompetence(competenceName)
                .orElseThrow(() -> new ExecutionException("No competence with such name"))
                .getOrderRequests().stream()
    private final OrderRequestExecutorRepo orderRequestExecutorRepo;

                .filter(orderRequest -> !orderRequest.getIsPrivate()
                        && orderRequest.getStatus().equals(OrderRequestStatus.opened))
                .map(OrderRequest::new).collect(Collectors.toList());
    }

    public List<OrderRequest> getPublicOrderRequestsByExecutorId(Integer executorId) {
        return executorRepo.findById(executorId).orElseThrow(() -> new ExecutionException("No executor with such id"))
                .getMyOrderRequests()
                .stream()
                .filter(orderRequestExecutor -> orderRequestExecutor.executorAgrIsSet() && orderRequestExecutor.getExecutorAgr())
                .map(orderRequestExecutor -> orderRequestExecutor.getPrimaryKey().getOrderRequest())
                .filter(orderRequest -> !orderRequest.getIsPrivate() && orderRequest.getStatus().equals(OrderRequestStatus.opened))
                .map(OrderRequest::new)
                .collect(Collectors.toList());
    }

    public List<OrderRequest> getPrivateOrderRequestsByExecutorId(Integer executorId) {
        return executorRepo.findById(executorId).orElseThrow(() -> new ExecutionException("No executor with such id"))
                .getMyOrderRequests()
                .stream()
                .filter(orderRequestExecutor -> !orderRequestExecutor.executorAgrIsSet())
                .map(orderRequestExecutor -> orderRequestExecutor.getPrimaryKey().getOrderRequest())
                .filter(orderRequest -> orderRequest.getIsPrivate() && orderRequest.getStatus().equals(OrderRequestStatus.opened))
                .map(OrderRequest::new)
                .collect(Collectors.toList());
    }


    @Transactional
    public void chooseOrderRequest(OrderRequest orderRequest, Integer executorId) {
        CompetenceEntity neededCompetence = competenceRepo.findByCompetence(orderRequest.getCompetence())
                .orElseThrow(() -> new ExecutionException("Incorrect competence name"));
        ExecutorEntity executor = executorRepo.findById(executorId)
                .orElseThrow(() -> new ExecutionException("No executor with such id"));
        if (!executor.getCompetences().contains(neededCompetence)) {
            throw new ExecutionException("Executor hasn't needed competence");
        }
        OrderRequestEntity orderRequestEntity = orderRequestRepo.findById(orderRequest.getId())
                .orElseThrow(() -> new ExecutionException("No orderRequest with such id"));

        OrderRequestExecutorId primaryKey = new OrderRequestExecutorId(orderRequestEntity, executor);
        OrderRequestExecutorEntity orderRequestExecutor = orderRequestExecutorRepo
                .findByPrimaryKey(primaryKey).orElse(null);
        if (orderRequestExecutor != null) {
            if (orderRequestExecutor.getExecutorAgr()) {
                throw new ExecutionException("executor has already given agr to this orderRequest");
            }
            orderRequestExecutor.setExecutorAgr(true);
            orderRequestExecutorRepo.save(orderRequestExecutor);
        } else {
            orderRequestEntity.addExecutor(executor, null, true);
            orderRequestRepo.save(orderRequestEntity);
        }

    }

    private OrderRequestExecutorEntity getOrderRequestExecutorEntityByIds(Integer orderRequestId,
                                                                          Integer executorId) {
        OrderRequestEntity orderRequest = orderRequestRepo.findById(orderRequestId)
                .orElseThrow(() -> new ExecutionException("No orderRequest with such id"));

        ExecutorEntity executor = executorRepo.findById(executorId)
                .orElseThrow(() -> new ExecutionException("No executor with such id"));

        OrderRequestExecutorId primaryKey = new OrderRequestExecutorId(orderRequest, executor);

        return orderRequestExecutorRepo.findByPrimaryKey(primaryKey)
                .orElseThrow(() -> new ExecutionException("no such orderRequestExecutor"));
    }

    public void revokeAgrToPublicOrderRequest(Integer executorId, Integer orderRequestId) {
        var orderRequestExecutor = getOrderRequestExecutorEntityByIds(orderRequestId, executorId);
        if (!orderRequestExecutor.executorAgrIsSet()) {
            throw new ExecutionException("can't revoke, executor agr is null");
        }
        if (!orderRequestExecutor.getExecutorAgr()) {
            throw new ExecutionException("can't revoke, executor agr is false");
        }
        orderRequestExecutor.setExecutorAgr(false);
        orderRequestExecutorRepo.save(orderRequestExecutor);
    }


    public void declinePrivateOrderRequest(Integer executorId, Integer orderRequestId) {
        var orderRequestExecutor = getOrderRequestExecutorEntityByIds(orderRequestId, executorId);
        if (orderRequestExecutor.executorAgrIsSet()) {
            throw new ExecutionException("can't decline, executor agr is set");
        }
        orderRequestExecutor.setExecutorAgr(false);
        orderRequestExecutorRepo.save(orderRequestExecutor);
    }

    public void acceptPrivateOrderRequest(Integer executorId, Integer orderRequestId) {
        var orderRequestExecutor = getOrderRequestExecutorEntityByIds(orderRequestId, executorId);
        if (orderRequestExecutor.executorAgrIsSet()) {
            throw new ExecutionException("can't accept, executor agr is set");
        }
        orderRequestExecutor.setExecutorAgr(true);
        orderRequestExecutorRepo.save(orderRequestExecutor);
    }

    public List<OrderDTO> getExecutorOrdersWithNoFeedback(Integer executorId) {
        return executorRepo.findById(executorId).orElseThrow(() -> new ExecutionException("no executor with such id"))
                .getMyOrders().stream().filter(o -> !o.clientSentFeedback(FeedbackId.ClientType.executor))
                .map(OrderDTO::new).collect(Collectors.toList());
    }

    public void sendFeedback(FeedbackDTO feedback, Integer executorId) {
        OrderEntity order = orderRepo.findById(feedback.getOrderId())
                .orElseThrow(() -> new ExecutionException("no order with such id"));
        if(order.getExecutor().getId() != executorId){
            throw new ExecutionException("this order doesn't belong to executor");
        }
        if (order.clientSentFeedback(FeedbackId.ClientType.executor)) {
            throw new ExecutionException("feedback from executor was already sent");
        }
        order.addFeedback(feedback, FeedbackId.ClientType.executor);
        orderRepo.save(order);
    }

    public Boolean isCompetenceMatchesExecutor(Integer executorId, String competenceName) {
        var competence = competenceRepo.findByCompetence(competenceName)
                .orElseThrow(() -> new IllegalArgumentException("No competence with such name"));
        return executorRepo.findById(executorId)
                .orElseThrow(() -> new IllegalArgumentException("No executor with such id"))
                .getCompetences().contains(competence);
    }


    private CustomerEntity getCustomerEntityById(Integer customerId) {
        return customerRepo.findById(customerId)
                .orElseThrow(()->new IllegalArgumentException("no executor with such id"));
    }

    public Customer getCustomerById(Integer executorId){
        return new Customer(getCustomerEntityById(executorId));
    }

    public List<FeedbackDTO> getAllCustomerFeedbacks(Integer customerId){
        var customer = getCustomerEntityById(customerId);
        return orderRepo.findAllFeedbackAboutCustomer(customer)
                .stream().map(FeedbackDTO::new).collect(Collectors.toList());
    }
}