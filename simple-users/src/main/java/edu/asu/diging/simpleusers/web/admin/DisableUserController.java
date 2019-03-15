package edu.asu.diging.simpleusers.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.core.service.IUserManager;

@Controller
public class DisableUserController extends ManageUserController {
    
    protected final static String USERNAME_VARIABLE = "username";
    public final static String REQUEST_MAPPING_PATH = "{" + USERNAME_VARIABLE + "}/disable";
    
    @Autowired
    private IUserManager userManager;
    
    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    protected String getMappingPath() {
        return REQUEST_MAPPING_PATH;
    }

    @Override
    protected ModelAndView handlePost(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = getUsername(request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        userManager.disableUser(username, authentication.getName());
        
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:" + configProvider.getUserEndpointPrefix() + ListUsersController.REQUEST_MAPPING_PATH);
        return model;
    }

    @Override
    protected ModelAndView handleGet(HttpServletRequest request, HttpServletResponse response) throws Exception {
        throw new MethodNotSupportedException(RequestMethod.GET);
    }

    @Override
    protected String getFailureViewName() {
        return "redirect:" + configProvider.getUserEndpointPrefix() + ListUsersController.REQUEST_MAPPING_PATH;
    }
}