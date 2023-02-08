package databases.itmo.coursework.security.checkers;

import databases.itmo.coursework.model.UserRole;
import databases.itmo.coursework.security.UserPrincipal;
import org.springframework.stereotype.Component;

@Component
public abstract class UserRoleChecker {
    private  UserRoleChecker next;

    public static UserRoleChecker link(UserRoleChecker first, UserRoleChecker ... chain){
        UserRoleChecker node = first;
        for(UserRoleChecker nextInChain : chain){
            node.next = nextInChain;
            node = nextInChain;
        }
        return first;
    }

    public abstract UserPrincipal check(Integer personId);

    protected UserPrincipal checkNext(Integer personId){
        if(next == null){
            //UserPrincipal user = new UserPrincipal();
            //user.setUserRole(UserRole.NONE);
            return null;
        }
        return next.check(personId);
    }
}
