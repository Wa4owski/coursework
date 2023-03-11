package databases.itmo.coursework.servises;

import databases.itmo.coursework.entities.TicketEntity;
import databases.itmo.coursework.model.Ticket;
import databases.itmo.coursework.repo.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ModeratorService {

    @Autowired
    TicketRepo ticketRepo;

    public List<Ticket> getTicketsByModeratorId(Integer moderatorId) {
        return ticketRepo.findAll().stream()
                .filter(o -> o.getModeratorId() == moderatorId)
                .filter(o -> o.getStatus().equals(TicketEntity.TicketStatus.opened))
                .map(Ticket::new).collect(Collectors.toList());
    }
}
