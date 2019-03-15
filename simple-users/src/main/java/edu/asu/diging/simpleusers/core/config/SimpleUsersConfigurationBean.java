package edu.asu.diging.simpleusers.core.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;

import edu.asu.diging.simpleusers.web.admin.ApproveAccountController;
import edu.asu.diging.simpleusers.web.admin.DisableUserController;
import edu.asu.diging.simpleusers.web.admin.ListUsersController;
import edu.asu.diging.simpleusers.web.admin.RemoveAdminRoleController;
import edu.asu.diging.simpleusers.web.admin.AddAdminRoleController;

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
        mapping.setUrlMap(urlMappings);
        return mapping;
    }
}
