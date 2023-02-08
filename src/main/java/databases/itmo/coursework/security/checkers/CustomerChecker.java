package databases.itmo.coursework.security.checkers;

import databases.itmo.coursework.entities.CustomerEntity;
import databases.itmo.coursework.model.UserRole;
import databases.itmo.coursework.model.UserStatus;
import databases.itmo.coursework.repo.CustomerRepo;
import databases.itmo.coursework.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomerChecker extends UserRoleChecker{
    @Autowired
    CustomerRepo repository;

    @Override
    public UserPrincipal check(Integer personId) {
        final Optional<CustomerEntity> customerOp = repository.findByPersonId(personId);
        if(customerOp.isPresent()){
            UserPrincipal user = new UserPrincipal();
            user.setUserRole(UserRole.ROLE_CUSTOMER);
            user.setBanned(customerOp.get().getStatus().equals(UserStatus.banned));
            user.setUserSpecId(customerOp.get().getId());
            return user;
        }
        return checkNext(personId);
    }
}
