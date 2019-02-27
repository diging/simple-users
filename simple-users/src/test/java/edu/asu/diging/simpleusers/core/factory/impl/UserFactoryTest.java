package edu.asu.diging.simpleusers.core.factory.impl;

import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.web.forms.UserForm;

public class UserFactoryTest {
    
    @InjectMocks
    private UserFactory factoryToTest;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void test_createUser_withForm() {
        String username = "username";
        String firstName = "firstName";
        String lastName = "lastName";
        String password = "password";
        String email = "email";
        
        UserForm form = new UserForm();
        form.setUsername(username);
        form.setEmail(email);
        form.setFirstName(firstName);
        form.setLastName(lastName);
        form.setPassword(password);
        
        IUser user = factoryToTest.createUser(form);
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(firstName, user.getFirstName());
        Assertions.assertEquals(lastName, user.getLastName());
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertEquals(email, user.getEmail());
    }
    
    @Test
    public void test_createUser_withStringsAndEnabled() {
        String username = "username";
        String password = "password";
        String role = "ROLE";
        boolean enabled = true;
        
        IUser user = factoryToTest.createUser(username, password, role, enabled);
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertTrue(((UserDetails)user).isEnabled());
        Assertions.assertTrue(((UserDetails)user).isAccountNonExpired());
        Assertions.assertTrue(((UserDetails)user).isAccountNonLocked());
        Assertions.assertTrue(((UserDetails)user).isCredentialsNonExpired());
        
        Set<SimpleGrantedAuthority> roles = user.getRoles();
        Assertions.assertEquals(1, roles.size());
        Assertions.assertEquals(role, roles.iterator().next().getAuthority());
    }
    
    @Test
    public void test_createUser_withStringsAndDisables() {
        String username = "username";
        String password = "password";
        String role = "ROLE";
        boolean enabled = false;
        
        IUser user = factoryToTest.createUser(username, password, role, enabled);
        Assertions.assertEquals(username, user.getUsername());
        Assertions.assertEquals(password, user.getPassword());
        Assertions.assertFalse(((UserDetails)user).isEnabled());
    }
}
