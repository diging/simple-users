package edu.asu.diging.simpleusers.core.service.impl;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.data.TokenRepository;
import edu.asu.diging.simpleusers.core.exceptions.InvalidTokenException;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.PasswordResetToken;
import edu.asu.diging.simpleusers.core.model.impl.User;
import edu.asu.diging.simpleusers.core.service.IEmailService;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import edu.asu.diging.simpleusers.core.service.SimpleUsersConstants;

public class TokenServiceTest {

    @Mock
    private IUserManager userManager;
    
    @Mock
    private ConfigurationProvider configProvider;
    
    @Mock
    private TokenRepository tokenRepo;
    
    @Mock
    private IEmailService emailService;
    
    @InjectMocks
    private TokenService serviceToTest;
    
    private IUser user;
    private String USERNAME = "username";
    private String EMAIL = "email@test.com";
    
    private String TOKEN = "token";
    private PasswordResetToken resetToken;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        user = new User();
        user.setUsername(USERNAME);
        user.setEmail(EMAIL);
        
        Mockito.when(userManager.findByEmail(EMAIL)).thenReturn(user);
        
        resetToken = new PasswordResetToken();
        resetToken.setToken(TOKEN);
        resetToken.setUser(user);
        resetToken.setExpiryDate(OffsetDateTime.now().plusHours(1));
        
        
        Mockito.when(tokenRepo.findByToken(TOKEN)).thenReturn(resetToken);
    }
    
    @Test
    public void test_resetPassword_success() throws UserDoesNotExistException, IOException, MessagingException {
        serviceToTest.resetPassword(EMAIL);
        
        Mockito.verify(tokenRepo).save(Mockito.any(PasswordResetToken.class));
        Mockito.verify(emailService).sendResetPasswordEmail(Mockito.eq(user), Mockito.any(PasswordResetToken.class));
    }
    
    @Test
    public void test_resetPassword_emailDoesNotExist() {
        Assertions.assertThrows(UserDoesNotExistException.class, () -> serviceToTest.resetPassword("does@not.exist"));
    }
    
    @Test
    public void test_validateToken_success() throws InvalidTokenException, TokenExpiredException {
        boolean success = serviceToTest.validateToken(TOKEN, USERNAME);
        
        Assertions.assertTrue(success);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Assertions.assertEquals(user, auth.getPrincipal());
        
        Assertions.assertEquals(new SimpleGrantedAuthority("ROLE_" + SimpleUsersConstants.CHANGE_PASSWORD_ROLE), auth.getAuthorities().iterator().next());
    }
    
    @Test
    public void test_validateToken_invalidToken() {
        Assertions.assertThrows(InvalidTokenException.class, () -> serviceToTest.validateToken(TOKEN, "wrongUser"));
    }
    
    @Test
    public void test_validateToken_tokenExpired() throws InterruptedException {
        PasswordResetToken expiredToken = new PasswordResetToken();
        expiredToken.setToken("token2");
        expiredToken.setUser(user);
        expiredToken.setExpiryDate(OffsetDateTime.now().plusSeconds(1));
        
        Mockito.when(tokenRepo.findByToken("token2")).thenReturn(expiredToken);
        
        TimeUnit.SECONDS.sleep(1);
        Assertions.assertThrows(TokenExpiredException.class, () -> serviceToTest.validateToken("token2", USERNAME));
    }
}
