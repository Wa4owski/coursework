package databases.itmo.coursework.security.checkers;

import databases.itmo.coursework.entities.ModeratorEntity;
import databases.itmo.coursework.model.UserRole;
import databases.itmo.coursework.repo.ModeratorEntityRepo;
import databases.itmo.coursework.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ModeratorChecker extends UserRoleChecker{
    @Autowired
    ModeratorEntityRepo repository;

    @Override
    public UserPrincipal check(Integer personId) {
        final Optional<ModeratorEntity> moderatorOp = repository.findByPersonId(personId);
        if(moderatorOp.isPresent()){
            UserPrincipal user = new UserPrincipal();
            user.setUserRole(UserRole.ROLE_MODERATOR);
            user.setBanned(false);
            user.setUserSpecId(moderatorOp.get().getId());
            return user;
        }
        return checkNext(personId);
    }
}
