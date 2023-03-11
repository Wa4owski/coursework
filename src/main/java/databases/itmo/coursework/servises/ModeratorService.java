package databases.itmo.coursework.servises;

import databases.itmo.coursework.entities.OrderEntity;
import databases.itmo.coursework.entities.TicketEntity;
import databases.itmo.coursework.entities.VerdictEntity;
import databases.itmo.coursework.entities.keys.VerdictId;
import databases.itmo.coursework.model.Ticket;
import databases.itmo.coursework.model.Verdict;
import databases.itmo.coursework.repo.OrderRepo;
import databases.itmo.coursework.repo.TicketRepo;
import databases.itmo.coursework.repo.VerdictRepo;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeratorService {

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    VerdictRepo verdictRepo;

    @Autowired
    OrderRepo orderRepo;

    public List<Ticket> getTicketsByModeratorId(Integer moderatorId) {
        return ticketRepo.findAll().stream()
                .filter(o -> o.getModeratorId() == moderatorId)
                .filter(o -> o.getStatus().equals(TicketEntity.TicketStatus.opened))
                .map(Ticket::new).collect(Collectors.toList());
    }

    public void putVerdict(Verdict verdict) {
        OrderEntity order = orderRepo.findById(verdict.getOrderId())
                .orElseThrow(()->new ExecutionException("no order with such id"));
        VerdictEntity newVerdict = new VerdictEntity();
        newVerdict.setVerdictId(new VerdictId(order));
        newVerdict.setBanCustomer(verdict.getBan_customer());
        newVerdict.setBanExecutor(verdict.getBan_executor());
        newVerdict.setNewRateForCustomer(verdict.getNew_rate_for_customer());
        newVerdict.setNewRateForExecutor(verdict.getNew_rate_for_executor());
        newVerdict.setDeleteFeedbackAboutCustomer(verdict.getDelete_feedback_about_customer());
        newVerdict.setDeleteFeedbackAboutExecutor(verdict.getDelete_feedback_about_executor());
        verdictRepo.save(newVerdict);
    }
}
