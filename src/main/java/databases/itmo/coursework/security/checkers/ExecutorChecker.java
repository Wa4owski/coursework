package databases.itmo.coursework.security.checkers;


import databases.itmo.coursework.entities.ExecutorEntity;
import databases.itmo.coursework.model.UserRole;
import databases.itmo.coursework.model.UserStatus;
import databases.itmo.coursework.repo.ExecutorEntityRepo;
import databases.itmo.coursework.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class ExecutorChecker extends UserRoleChecker{
    @Autowired
    ExecutorEntityRepo repository;


    @Override
    public UserPrincipal check(Integer personId) {
        final Optional<ExecutorEntity> executorOp = repository.findByPersonId(personId);
        if(executorOp.isPresent()){
            UserPrincipal user = new UserPrincipal();
            user.setUserRole(UserRole.ROLE_EXECUTOR);
            user.setBanned(executorOp.get().getStatus().equals(UserStatus.banned));
            user.setUserSpecId(executorOp.get().getId());
            return user;
        }
        return checkNext(personId);
    }
}
