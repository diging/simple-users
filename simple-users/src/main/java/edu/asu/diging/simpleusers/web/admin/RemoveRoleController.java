package edu.asu.diging.simpleusers.web.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import edu.asu.diging.simpleusers.web.SimpleUserBaseController;

@Controller
public class RemoveRoleController extends SimpleUserBaseController {

public final static String REQUEST_MAPPING_PATH = "{" + USERNAME_VARIABLE + "}/role/remove";
    
    @Autowired
    private IUserManager userManager;
    
    @Autowired
    private ConfigurationProvider configProvider;

    @Override
    protected String getMappingPath() {
        return REQUEST_MAPPING_PATH;
    }

    @Override
    protected ModelAndView handlePost(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        String[] roles = request.getParameterValues("roles");
        if (roles != null && roles.length > 0) {
            String username = getUsername(request);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            for (String role : roles) {
                userManager.removeRole(username, authentication.getName(), role);
            }
        }
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:" + configProvider.getUserEndpointPrefix() + ListUsersController.REQUEST_MAPPING_PATH);
        return model;
    }

    @Override
    protected ModelAndView handleGet(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        throw new MethodNotSupportedException(RequestMethod.GET);
    }

    @Override
    protected String getFailureViewName() {
        return "redirect:" + configProvider.getUserEndpointPrefix() + ListUsersController.REQUEST_MAPPING_PATH;
    }

}
