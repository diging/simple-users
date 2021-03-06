package edu.asu.diging.simpleusers.core.factory.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import edu.asu.diging.simpleusers.core.factory.IUserFactory;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import edu.asu.diging.simpleusers.web.forms.UserForm;

@Component
public class UserFactory implements IUserFactory {

    /* (non-Javadoc)
     * @see edu.asu.diging.sustainability.core.factory.impl.IUserFactory#createUser(edu.asu.diging.sustainability.web.pages.UserForm)
     */
    @Override
    public IUser createUser(UserForm userForm) {
        IUser user = new SimpleUser();
        user.setEmail(userForm.getEmail());
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setPassword(userForm.getPassword());
        user.setUsername(userForm.getUsername());
        
        return user;
    }
    
    @Override
    public IUser createUser(String username, String password, String role, boolean enabled) {
        IUser user = new SimpleUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(enabled);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role));
        user.setRoles(roles);
        return user;
    }
}