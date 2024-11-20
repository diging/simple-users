package edu.asu.diging.simpleusers.core.service.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.model.IPasswordResetToken;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
@PropertySource("classpath:/email.properties")
public class EmailService implements IEmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ConfigurationProvider configProvider;

    @Value("${_reset_email_subject}")
    private String emailSubject;

    /*
     * (non-Javadoc)
     * 
     * @see edu.asu.diging.simpleusers.core.service.impl.IEmailService#
     * sendResetPasswordEmail(java.lang.String,
     * edu.asu.diging.simpleusers.core.model.IUser,
     * edu.asu.diging.simpleusers.core.model.IPasswordResetToken)
     */
    @Override
    public void sendResetPasswordEmail(IUser user, IPasswordResetToken token) throws IOException, MessagingException {
        if (!(mailSender instanceof NotSetupMailSender)) {

            String body;
            if (configProvider.getEmailBody() != null && !configProvider.getEmailBody().trim().isEmpty()) {
                body = configProvider.getEmailBody();
            } else {
                InputStream resource = new ClassPathResource("/resetPasswordEmail.txt").getInputStream();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
                    body = reader.lines().collect(Collectors.joining("\n"));
                }
            }
            body = body.replace("$user", user.getFirstName() + " " + user.getLastName());
            body = body.replace("$app", configProvider.getAppName());

            String resetUrl = configProvider.getInstanceUrl();
            resetUrl = resetUrl + configProvider.getResetPasswordInitiatedEndpoint();
            resetUrl = resetUrl.replace("//", "/");
            resetUrl = resetUrl + "?token=" + token.getToken() + "&user=" + user.getUsername();
            body = body.replace("$url", resetUrl);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setTo(user.getEmail());
            helper.setFrom(configProvider.getEmailFrom());
            helper.setSubject(
                    configProvider.getEmailSubject() != null && !configProvider.getEmailSubject().trim().isEmpty()
                            ? configProvider.getEmailSubject()
                            : emailSubject);

            helper.setText(body, true);

            mailSender.send(message);
        }
    }
}
