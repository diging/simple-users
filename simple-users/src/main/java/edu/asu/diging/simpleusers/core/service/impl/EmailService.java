package edu.asu.diging.simpleusers.core.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
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
import edu.asu.diging.simpleusers.core.model.IUser;

@Service
@PropertySource("classpath:/email.properties")
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private SimpleMailMessage preconfiguredMessage;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Value("${_reset_email_subject}")
    private String emailSubject;
    
    public void sendResetPasswordEmail(String to, IUser user) throws IOException {
        if (!(mailSender instanceof NotSetupMailSender)) {
            
            File file = ResourceUtils.getFile("classpath:resetPasswordEmail.txt");
            String body = new String(Files.readAllBytes(file.toPath()));
            body = body.replace("$user", user.getFirstName() + " " + user.getLastName());
            body = body.replace("$app", configProvider.getAppName());
            
            String resetUrl = configProvider.getInstanceUrl();
            resetUrl = resetUrl + configProvider.getResetPasswordEndpoint();
            resetUrl = resetUrl.replace("//", "/");
            
            SimpleMailMessage message = new SimpleMailMessage(preconfiguredMessage);
            message.setTo(to);
            message.setSubject(emailSubject);
            message.setText(body);
            mailSender.send(message);
        }
    }
}
