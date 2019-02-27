package edu.asu.diging.simpleusers.core.config.impl;

import java.util.Iterator;
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
    
}
