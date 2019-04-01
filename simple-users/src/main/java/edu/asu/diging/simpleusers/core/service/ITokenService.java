package edu.asu.diging.simpleusers.core.service;

import java.io.IOException;

import javax.mail.MessagingException;

import edu.asu.diging.simpleusers.core.exceptions.InvalidTokenException;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;

public interface ITokenService {

    void resetPassword(String email) throws UserDoesNotExistException, IOException, MessagingException;

    boolean validateToken(String token, String username) throws InvalidTokenException, TokenExpiredException;

}