package edu.asu.diging.simpleusers.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.asu.diging.simpleusers.core.data.SimpleUserRepository;
import edu.asu.diging.simpleusers.core.exceptions.UserAlreadyExistsException;
import edu.asu.diging.simpleusers.core.factory.IUserFactory;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.SimpleUser;

public class UserServiceTest {

    @Mock
    private Environment env;
    
    @Mock
    private SimpleUserRepository userRepository;
    
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    
    @Mock
    private IUserFactory userFactory;
    
    @InjectMocks
    private UserService serviceToTest;
    
    private final String USER1_USERNAME = "user1";
    private final String USER2_USERNAME = "user2";
    private final String USER3_USERNAME = "user3";
    private final String USER4_USERNAME = "user4";
    
    private SimpleUser user1;
    
    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
        
        user1 = new SimpleUser();
        user1.setUsername(USER1_USERNAME);
        
        Mockito.when(userRepository.findById(USER1_USERNAME)).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findById(USER2_USERNAME)).thenReturn(Optional.empty());
        Mockito.when(userRepository.findById(USER3_USERNAME)).thenReturn(Optional.empty());
    }
    
    @Test
    public void test_loadUserByUsername_userInDb() {
        UserDetails actual = serviceToTest.loadUserByUsername(USER1_USERNAME);
        Assertions.assertEquals(user1, actual);
    }
    
    @Test
    public void test_loadUserByUsername_adminUser() {
        String userEntry = "password,ROLE_ADMIN,enabled";
        Mockito.when(env.getProperty(USER2_USERNAME)).thenReturn(userEntry);
        
        String password = "password";
        String role = "ROLE_ADMIN";
        IUser user = new SimpleUser();
        user.setUsername(USER2_USERNAME);
        user.setPassword(password);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role));
        user.setRoles(roles);
        
        Mockito.when(userFactory.createUser(USER2_USERNAME, password, role, true)).thenReturn(user);
        
        UserDetails actual = serviceToTest.loadUserByUsername(USER2_USERNAME);
        Assertions.assertEquals(user, actual);
    }
    
    @Test
    public void test_loadUserByUsername_doesNotExist() {
        Assertions.assertThrows(UsernameNotFoundException.class, () -> serviceToTest.loadUserByUsername(USER3_USERNAME));
    }
    
    @Test
    public void test_createUser_success() throws UserAlreadyExistsException {
        IUser user = new SimpleUser();
        user.setUsername(USER4_USERNAME);
        user.setPassword("password");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        serviceToTest.create(user);
        Mockito.verify(userRepository).save((SimpleUser)user);
    }
    
    @Test
    public void test_createUser_usernameInUse() {
        IUser user = new SimpleUser();
        user.setUsername(USER1_USERNAME);
        user.setPassword("password");
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        Assertions.assertThrows(UserAlreadyExistsException.class, () -> serviceToTest.create(user));
    }
    
    @Test
    public void test_findByUsername_exists() {
        IUser actual = serviceToTest.findByUsername(USER1_USERNAME);
        Assertions.assertEquals(user1, actual);
    }
    
    @Test
    public void test_findByUsername_doesNotExists() {
        IUser actual = serviceToTest.findByUsername(USER2_USERNAME);
        Assertions.assertNull(actual);
    }
    
    @Test
    public void test_findAll_success() {
        List<IUser> users = new ArrayList<>();
        users.add(user1);
        
        Mockito.when(userRepository.findAll()).thenReturn((Iterable)users);
        List<IUser> actual = serviceToTest.findAll();
        Assertions.assertEquals(users, actual);
    }
    
    @Test
    public void test_approveAccount() {
        String password = "password";
        IUser user = new SimpleUser();
        user.setUsername(USER3_USERNAME);
        user.setPassword(password);
        user.setEnabled(false);
        user.setAccountNonExpired(false);
        user.setAccountNonLocked(false);
        user.setCredentialsNonExpired(false);
        
        Mockito.when(userRepository.findById(USER3_USERNAME)).thenReturn(Optional.of((SimpleUser)user));
        
        serviceToTest.approveAccount(USER3_USERNAME, "approver");
        Mockito.verify(userRepository).save((SimpleUser)user);
        
        Assertions.assertTrue(((UserDetails)user).isEnabled());
        Assertions.assertTrue(((UserDetails)user).isAccountNonExpired());
        Assertions.assertTrue(((UserDetails)user).isAccountNonLocked());
        Assertions.assertTrue(((UserDetails)user).isCredentialsNonExpired());
    }
    
    @Test
    public void test_disableUser() {
        String password = "password";
        IUser user = new SimpleUser();
        user.setUsername(USER3_USERNAME);
        user.setPassword(password);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        Mockito.when(userRepository.findById(USER3_USERNAME)).thenReturn(Optional.of((SimpleUser)user));
        
        serviceToTest.disableUser(USER3_USERNAME, "approver");
        Mockito.verify(userRepository).save((SimpleUser)user);
        
        Assertions.assertFalse(((UserDetails)user).isEnabled());
    }
    
    @Test
    public void test_addRole() {
        String password = "password";
        IUser user = new SimpleUser();
        user.setUsername(USER3_USERNAME);
        user.setPassword(password);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        Mockito.when(userRepository.findById(USER3_USERNAME)).thenReturn(Optional.of((SimpleUser)user));
       
        String role = "ADMIN";
        serviceToTest.addRole(USER3_USERNAME, "initiator", role);
        Mockito.verify(userRepository).save((SimpleUser)user);
        Set<SimpleGrantedAuthority> roles = user.getRoles();
        Assertions.assertEquals(1, roles.size());
        Assertions.assertTrue(roles.iterator().next().getAuthority().equals(role));
    }
    
    @Test
    public void test_removeRole_roleExists() {
        String password = "password";
        IUser user = new SimpleUser();
        user.setUsername(USER3_USERNAME);
        user.setPassword(password);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        String role = "ADMIN";
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role));
        user.setRoles(roles);
        
        Mockito.when(userRepository.findById(USER3_USERNAME)).thenReturn(Optional.of((SimpleUser)user));
        
        serviceToTest.removeRole(USER3_USERNAME, "initiator", role);
        Mockito.verify(userRepository).save((SimpleUser)user);
        Set<SimpleGrantedAuthority> actualRoles = user.getRoles();
        Assertions.assertEquals(0, actualRoles.size());
    }
    
    @Test
    public void test_removeRole_roleDoesNotExists() {
        String password = "password";
        IUser user = new SimpleUser();
        user.setUsername(USER3_USERNAME);
        user.setPassword(password);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        
        String role = "ADMIN";
        Set<SimpleGrantedAuthority> roles = new HashSet<>();
        roles.add(new SimpleGrantedAuthority(role));
        user.setRoles(roles);
        
        Mockito.when(userRepository.findById(USER3_USERNAME)).thenReturn(Optional.of((SimpleUser)user));
        
        serviceToTest.removeRole(USER3_USERNAME, "initiator", "USER");
        Set<SimpleGrantedAuthority> actualRoles = user.getRoles();
        Assertions.assertEquals(1, actualRoles.size());
    }
}
