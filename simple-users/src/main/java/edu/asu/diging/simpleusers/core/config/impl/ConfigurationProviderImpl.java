package edu.asu.diging.simpleusers.core.config.impl;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.config.SimpleUsers;
import edu.asu.diging.simpleusers.core.config.SimpleUsersConfiguration;

@Service
public class ConfigurationProviderImpl implements ConfigurationProvider {

    private SimpleUsers config;
    
    @Autowired
    private ApplicationContext context;
    
    @PostConstruct
    public void init() {
        Map<String, SimpleUsersConfiguration> configs = context.getBeansOfType(SimpleUsersConfiguration.class);
        config = new BasicSimpleUsers();
        for (SimpleUsersConfiguration configuration : configs.values()) {
            configuration.configure(config);            
        } 
    }

    /* (non-Javadoc)
     * @see edu.asu.diging.simpleusers.core.config.impl.ConfigurationProvider#getUserListView()
     */
    @Override
    public String getUserListView() {
        return config.getUserListView();
    }

    @Override
    public String getRegisterView() {
        return config.getRegisterView();
    }
    
    @Override
    public String getSuccessRegistrationRedirect() {
        return config.getRegisterSuccessRedirect();
    }
    
    @Override
    public String getUserEndpointPrefix() {
        if (!config.getUsersEndpointPrefix().endsWith("/")) {
            return config.getUsersEndpointPrefix() + "/";
        }
        return config.getUsersEndpointPrefix();
    }
    
    @Override
    public String getFullEndpoint(String controllerEndpoint) {
        String endpointPrefix = context.getApplicationName();
        if (!endpointPrefix.endsWith("/") && !getUserEndpointPrefix().startsWith("/")) {
            endpointPrefix = endpointPrefix + "/" + getUserEndpointPrefix();
        } else {
            endpointPrefix = (endpointPrefix + getUserEndpointPrefix()).replaceAll("//", "/");
        }
        return endpointPrefix + controllerEndpoint;
    }
    
    @Override
    public String getResetPasswordEndpoint() {
        return config.getResetPasswordEndpoint();
    }
    
    @Override
    public long getTokenExpirationPeriod() {
        return config.getTokenExpirationPeriod();
    }
    
    @Override
    public String getEmailUsername() {
        return config.getEmailUsername();
    }

    @Override
    public String getEmailPassword() {
        return config.getEmailPassword();
    }

    @Override
    public String getEmailServerPort() {
        return config.getEmailServerPort();
    }

    @Override
    public String getEmailServerHost() {
        return config.getEmailServerHost();
    }
    
    @Override
    public String getEmailProtocol() {
        return config.getEmailProtocol();
    }

    @Override
    public boolean isEmailAuthentication() {
        return config.isEmailAuthentication();
    }

    @Override
    public boolean isEmailStartTlsEnable() {
        return config.isEmailStartTlsEnable();
    }

    @Override
    public boolean isEmailDebug() {
        return config.isEmailDebug();
    }
    
    @Override
    public String getInstanceUrl() {
        return config.getInstanceUrl();
    }
    
    @Override
    public String getEmailFrom() {
        return config.getEmailFrom();
    }
    
    @Override
    public String getAppName() {
        return config.getAppName();
    }
}
