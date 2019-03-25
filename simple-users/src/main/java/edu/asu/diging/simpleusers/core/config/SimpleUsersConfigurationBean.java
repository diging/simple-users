package edu.asu.diging.simpleusers.core.config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import edu.asu.diging.simpleusers.web.admin.ApproveAccountController;
import edu.asu.diging.simpleusers.web.admin.DisableUserController;
import edu.asu.diging.simpleusers.web.admin.ListUsersController;
import edu.asu.diging.simpleusers.web.admin.RemoveAdminRoleController;
import edu.asu.diging.simpleusers.web.admin.RemoveRoleController;
import edu.asu.diging.simpleusers.core.service.impl.NotSetupMailSender;
import edu.asu.diging.simpleusers.web.admin.AddAdminRoleController;
import edu.asu.diging.simpleusers.web.admin.AddRoleController;

@Configuration
public class SimpleUsersConfigurationBean {

    @Autowired
    private ConfigurationProvider configProvider;
    
    @Bean
    public SimpleUrlHandlerMapping handlerMapping() {
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        
        String endpoint = configProvider.getUserEndpointPrefix();
        Map<String, Object> urlMappings = new HashMap<>();
        urlMappings.put(endpoint + ListUsersController.REQUEST_MAPPING_PATH, "listUsersController");
        urlMappings.put(endpoint + ApproveAccountController.REQUEST_MAPPING_PATH, "approveAccountController");
        urlMappings.put(endpoint + AddAdminRoleController.REQUEST_MAPPING_PATH, "addAdminRoleController");
        urlMappings.put(endpoint + RemoveAdminRoleController.REQUEST_MAPPING_PATH, "removeAdminRoleController");
        urlMappings.put(endpoint + DisableUserController.REQUEST_MAPPING_PATH, "disableUserController");
        urlMappings.put(endpoint + AddRoleController.REQUEST_MAPPING_PATH, "addRoleController");
        urlMappings.put(endpoint + RemoveRoleController.REQUEST_MAPPING_PATH, "removeRoleController");
        mapping.setUrlMap(urlMappings);
        return mapping;
    }
    
    @Bean
    public JavaMailSender javaMailSender() {
        if (configProvider.getEmailServerHost() == null || configProvider.getEmailServerHost().isEmpty()) {
            return new NotSetupMailSender(); 
        }
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(configProvider.getEmailServerHost());
        sender.setPort(new Integer(configProvider.getEmailServerPort()));
        sender.setPassword(configProvider.getEmailPassword());
        sender.setUsername(configProvider.getEmailUsername());
        
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.transport.protocol", configProvider.getEmailProtocol());
        javaMailProperties.put("mail.smtp.auth", configProvider.isEmailAuthentication());
        javaMailProperties.put("mail.smtp.starttls.enable", configProvider.isEmailStartTlsEnable());
        javaMailProperties.put("mail.debug", configProvider.isEmailDebug());
        sender.setJavaMailProperties(javaMailProperties);
        
        return sender;
    }
    
    @Bean
    public SimpleMailMessage preconfiguredEmailMessage() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(configProvider.getEmailFrom());
        return msg;
    }
}
