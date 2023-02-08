package databases.itmo.coursework.security;

import databases.itmo.coursework.entities.PersonEntity;
import databases.itmo.coursework.repo.PersonEntityRepo;
import databases.itmo.coursework.security.checkers.CustomerChecker;
import databases.itmo.coursework.security.checkers.ExecutorChecker;
import databases.itmo.coursework.security.checkers.ModeratorChecker;
import databases.itmo.coursework.security.checkers.UserRoleChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    PersonEntityRepo personEntityRepo;

    @Autowired
    CustomerChecker customerChecker;

    @Autowired
    ExecutorChecker executorChecker;

    @Autowired
    ModeratorChecker moderatorChecker;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       final PersonEntity person = personEntityRepo.findByEmail(username).orElseThrow(
               ()->new UsernameNotFoundException("No user with such email.")
       );
        UserRoleChecker userRoleChecker = UserRoleChecker.link(
                customerChecker,
                executorChecker,
                moderatorChecker
        );
        UserPrincipal userPrincipal = userRoleChecker.check(person.getId());
        if(userPrincipal == null){
            throw new UsernameNotFoundException("User has no role");
        }
        userPrincipal.setPersonEntity(person);
        return userPrincipal;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
