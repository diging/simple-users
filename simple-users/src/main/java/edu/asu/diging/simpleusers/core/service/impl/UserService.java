package edu.asu.diging.simpleusers.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.asu.diging.simpleusers.core.data.SimpleUserRepository;
import edu.asu.diging.simpleusers.core.exceptions.UserAlreadyExistsException;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;
import edu.asu.diging.simpleusers.core.factory.IUserFactory;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.Role;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;


@Transactional
@Service
@PropertySource("classpath:/user.properties")
public class UserService implements UserDetailsService, IUserManager {
    
    @Autowired
    private Environment env;
    
    @Autowired
    private SimpleUserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Autowired
    private IUserFactory userFactory;

    @Override
    public UserDetails loadUserByUsername(String arg0) throws UsernameNotFoundException {
        Optional<SimpleUser> foundUser = userRepository.findById(arg0);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        
        String userDetails = env.getProperty(arg0);
        if (userDetails != null) {
            String[] details = userDetails.split(",");
            return (UserDetails) userFactory.createUser(arg0, details[0], details[1], (details[2].equals("enabled")));
        }
        
        throw new UsernameNotFoundException(String.format("No user with username %s found.", arg0));
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.sustainability.core.service.impl.IUserManager#save(edu.asu.diging.sustainability.core.model.impl.User)
     */
    @Override
    public void create(IUser user) throws UserAlreadyExistsException {
        Optional<SimpleUser> existingUser = userRepository.findById(user.getUsername());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("The user already exists.");
        }
        user.setEnabled(false);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save((SimpleUser)user);
    }
    
    @Override
    public void changePassword(IUser user, String password) throws UserDoesNotExistException {
        Optional<SimpleUser> existingUser = userRepository.findById(user.getUsername());
        if (!existingUser.isPresent()) {
            throw new UserDoesNotExistException("User " + user.getUsername() + " does not exist.");
        }
        user = existingUser.get();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save((SimpleUser)user);
    }
    
    /* (non-Javadoc)
     * @see edu.asu.diging.sustainability.core.service.impl.IUserManager#findByUsername(java.lang.String)
     */
    @Override
    public IUser findByUsername(String username) {
        Optional<SimpleUser> foundUser = userRepository.findById(username);
        if (foundUser.isPresent()) {
            return foundUser.get();
        }
        return null;
    }
    
    @Override
    public IUser findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    @Override
    public List<IUser> findAll() {
        Iterable<SimpleUser> users = userRepository.findAll();
        List<IUser> results = new ArrayList<>();
        users.iterator().forEachRemaining(u -> results.add(u));
        return results;
    }
    
    @Override
    public void approveAccount(String username, String approver) {
        IUser user = findByUsername(username);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        if (user.getNotes() == null) {
            user.setNotes("");
        }
        user.setNotes(user.getNotes() + String.format("Approved by %s. ", approver));
        if (((IUser)user).getRoles() == null) {
            ((IUser)user).setRoles(new HashSet<>());
        }
        ((IUser)user).getRoles().add(new SimpleGrantedAuthority(Role.USER));
        userRepository.save((SimpleUser)user);
    }
    
    @Override
    public void disableUser(String username, String initiator) {
        IUser user = findByUsername(username);
        user.setEnabled(false);
        user.setNotes(user.getNotes() + String.format("Disabled by %s. ", initiator));
        userRepository.save((SimpleUser)user);
    }
    
    @Override
    public void addRole(String username, String initiator, String role) {
        IUser user = findByUsername(username);
        user.setNotes(user.getNotes() + String.format("User %s added role %s. ", initiator, role));
        if (((IUser)user).getRoles() == null) {
            ((IUser)user).setRoles(new HashSet<>());
        }
        ((IUser)user).getRoles().add(new SimpleGrantedAuthority(role));
        userRepository.save((SimpleUser)user);
    }
    
    @Override
    public void removeRole(String username, String initiator, String role) {
        IUser user = findByUsername(username);
        user.setNotes(user.getNotes() + String.format("User %s removed role %s. ", initiator, role));
        if (user.getRoles() == null) {
            return;
        }
        SimpleGrantedAuthority roleToBeRemoved = null;
        for (SimpleGrantedAuthority authority : user.getRoles()) {
            if (authority.getAuthority().equals(role)) {
                roleToBeRemoved = authority;
                break;
            }
        }
        
        if (roleToBeRemoved != null) {
            user.getRoles().remove(roleToBeRemoved);
            userRepository.save((SimpleUser)user);
        }
    }
}