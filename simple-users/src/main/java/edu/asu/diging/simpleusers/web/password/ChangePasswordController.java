package edu.asu.diging.simpleusers.web.password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import edu.asu.diging.simpleusers.web.SimpleUserBaseController;

@Controller
public class ChangePasswordController extends SimpleUserBaseController {
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private IUserManager userManager;

    @Override
    protected String getMappingPath() {
        return null;
    }

    @Override
    protected ModelAndView handlePost(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        Object userObj = SecurityContextHolder.getContext()
                                            .getAuthentication().getPrincipal();
        if (userObj == null || !(userObj instanceof IUser)) {
            return generateFailureModel("notAuthenticated");
        }
        
        IUser user = (IUser) userObj;
        String password = request.getParameter("password");
        if (password == null || password.trim().isEmpty()) {
            ModelAndView model = new ModelAndView();
            model.setViewName("redirect:" + configProvider.getChangePasswordEndpoint() + "?error=noPassword");
            return model;
        }
        
        String repeatedPassword = request.getParameter("passwordRepeat");
        if (!password.equals(repeatedPassword)) {
            ModelAndView model = new ModelAndView();
            model.setViewName("redirect:" + configProvider.getChangePasswordEndpoint() + "?error=notMatching");
            return model;
        }
        
        userManager.changePassword(user, password);
        
        request.logout();
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/" + "?success=PasswordChanged");
        return model;
    }

    @Override
    protected ModelAndView handleGet(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        ModelAndView model = new ModelAndView();
        model.setViewName(configProvider.getChangePasswordView());
        return model;
    }

    @Override
    protected String getFailureViewName() {
        return "redirect:/";
    }

}
