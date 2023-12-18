package databases.itmo.coursework.repo;

import databases.itmo.coursework.entities.CustomerEntity;
import databases.itmo.coursework.entities.ExecutorEntity;
import databases.itmo.coursework.entities.FeedbackEntity;
import databases.itmo.coursework.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepo extends JpaRepository<OrderEntity, Integer> {

    @Query("select f from FeedbackEntity f where f.feedbackId.author = 'customer'" +
            "and f.feedbackId.order.executor = :executor")
    List<FeedbackEntity> findAllFeedbackAboutExecutor(@Param("executor") ExecutorEntity executor);

    @Query("select f from FeedbackEntity f where f.feedbackId.author = 'executor'" +
            "and f.feedbackId.order.customer = :customer")
    List<FeedbackEntity> findAllFeedbackAboutCustomer(@Param("customer") CustomerEntity customer);
}
