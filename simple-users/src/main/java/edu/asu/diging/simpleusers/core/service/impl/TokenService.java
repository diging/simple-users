package edu.asu.diging.simpleusers.core.service.impl;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.data.TokenRepository;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;
import edu.asu.diging.simpleusers.core.model.IPasswordResetToken;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.impl.PasswordResetToken;
import edu.asu.diging.simpleusers.core.service.IUserManager;

@Service
public class TokenService {
    
    @Autowired
    private IUserManager userManager;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private TokenRepository tokenRepo;

    public void resetPassword(String email) throws UserDoesNotExistException {
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
    }
}
