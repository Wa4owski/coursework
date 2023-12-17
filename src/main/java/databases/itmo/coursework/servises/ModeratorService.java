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
import lombok.AllArgsConstructor;
import org.hibernate.sql.exec.ExecutionException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ModeratorService {

    private final TicketRepo ticketRepo;

    private final VerdictRepo verdictRepo;

    private final OrderRepo orderRepo;

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
        newVerdict.setBanCustomer(verdict.getBanCustomer());
        newVerdict.setBanExecutor(verdict.getBanExecutor());
        newVerdict.setNewRateForCustomer(verdict.getNewRateForCustomer());
        newVerdict.setNewRateForExecutor(verdict.getNewRateForExecutor());
        newVerdict.setDeleteFeedbackAboutCustomer(verdict.getDeleteFeedbackAboutCustomer());
        newVerdict.setDeleteFeedbackAboutExecutor(verdict.getDeleteFeedbackAboutExecutor());
        verdictRepo.save(newVerdict);
    }
}
