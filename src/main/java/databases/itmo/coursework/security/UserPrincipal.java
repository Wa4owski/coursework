package databases.itmo.coursework.security;

import databases.itmo.coursework.entities.PersonEntity;
import databases.itmo.coursework.model.UserRole;
import databases.itmo.coursework.security.checkers.CustomerChecker;
import databases.itmo.coursework.security.checkers.UserRoleChecker;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserPrincipal implements UserDetails {
   private PersonEntity personEntity;

    private UserRole userRole;
//    public UserPrincipal(PersonEntity personEntity) {
//        this.personEntity = personEntity;
//        UserRoleChecker checker = UserRoleChecker.link(new CustomerChecker());
//        this.userRole = checker.check(personEntity.getId());
//    }


    private boolean isBanned;
    private int userSpecId;
    //private List<GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userRole.name()));
        //return List.of(new SimpleGrantedAuthority("ADMIN"), new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Override
    public String getPassword() {
        return personEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return personEntity.getFullName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isBanned;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
