package edu.asu.diging.simpleusers.core.service;

import java.io.IOException;

import jakarta.mail.MessagingException;

import edu.asu.diging.simpleusers.core.model.IPasswordResetToken;
import edu.asu.diging.simpleusers.core.model.IUser;

public interface IEmailService {

    void sendResetPasswordEmail(IUser user, IPasswordResetToken token) throws IOException, MessagingException;

}