package edu.asu.diging.simpleusers.core.service.impl;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.UUID;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.data.TokenRepository;
import edu.asu.diging.simpleusers.core.exceptions.InvalidTokenException;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;
import edu.asu.diging.simpleusers.core.model.IPasswordResetToken;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.PasswordResetToken;
import edu.asu.diging.simpleusers.core.service.IEmailService;
import edu.asu.diging.simpleusers.core.service.ITokenService;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import edu.asu.diging.simpleusers.core.service.SimpleUsersConstants;

@Service
public class TokenService implements ITokenService {
    
    @Autowired
    private IUserManager userManager;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private TokenRepository tokenRepo;
    
    @Autowired
    private IEmailService emailService;

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.service.impl.ITokenService#resetPassword(java.lang.String)
     */
    @Override
    public void resetPassword(String email) throws UserDoesNotExistException, IOException, MessagingException {
        IUser user = userManager.findByEmail(email);
        if (user == null) {
            throw new UserDoesNotExistException("User with email " + email + " does not exist.");
        }
        IPasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        OffsetDateTime currentTime = OffsetDateTime.now();
        OffsetDateTime expirationTime = currentTime.plusMinutes(configProvider.getTokenExpirationPeriod());
        token.setExpiryDate(expirationTime);
        tokenRepo.save((PasswordResetToken)token);
        
        emailService.sendResetPasswordEmail(user, token);
    }
    
    @Override
    public boolean validateToken(String token, String username) throws InvalidTokenException, TokenExpiredException {
        PasswordResetToken resetToken = tokenRepo.findByToken(token);
        if (resetToken == null || !resetToken.getUser().getUsername().equals(username)) {
            throw new InvalidTokenException("Token is invalid.");
        }
        
        if (resetToken.getExpiryDate().isBefore(OffsetDateTime.now())) {
            throw new TokenExpiredException("Token expired on " + resetToken.getExpiryDate().toString());
        }
        
        IUser user = resetToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
          user, null, Arrays.asList(
          new SimpleGrantedAuthority("ROLE_" + SimpleUsersConstants.CHANGE_PASSWORD_ROLE)));
        SecurityContextHolder.getContext().setAuthentication(auth);
        return true;
    }
}
