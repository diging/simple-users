package edu.asu.diging.simpleusers.core.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.model.IPasswordResetToken;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IEmailService;

@Service
@PropertySource("classpath:/email.properties")
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private SimpleMailMessage preconfiguredMessage;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Value("${_reset_email_subject}")
    private String emailSubject;
    
    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.service.impl.IEmailService#sendResetPasswordEmail(java.lang.String, edu.asu.diging.simpleusers.core.model.IUser, edu.asu.diging.simpleusers.core.model.IPasswordResetToken)
     */
    @Override
    public void sendResetPasswordEmail(IUser user, IPasswordResetToken token) throws IOException {
        if (!(mailSender instanceof NotSetupMailSender)) {
            
            String body;
            if (configProvider.getEmailBody() != null && !configProvider.getEmailBody().trim().isEmpty()) {
                body = configProvider.getEmailBody();
            } else {
                File file = ResourceUtils.getFile("classpath:resetPasswordEmail.txt");
                body = new String(Files.readAllBytes(file.toPath()));
            }
            body = body.replace("$user", user.getFirstName() + " " + user.getLastName());
            body = body.replace("$app", configProvider.getAppName());
            
            String resetUrl = configProvider.getInstanceUrl();
            resetUrl = resetUrl + configProvider.getResetPasswordEndpoint();
            resetUrl = resetUrl.replace("//", "/");
            resetUrl = resetUrl + "?token=" + token.getToken() + "&user=" + user.getUsername();
            body = body.replace("$url", resetUrl);
            
            SimpleMailMessage message = new SimpleMailMessage(preconfiguredMessage);
            message.setTo(user.getEmail());
            message.setSubject(configProvider.getEmailSubject() != null && !configProvider.getEmailSubject().trim().isEmpty() ? configProvider.getEmailSubject() : emailSubject);
            message.setText(body);
            mailSender.send(message);
        }
    }
}
