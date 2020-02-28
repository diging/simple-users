package edu.asu.diging.simpleusers.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;
import edu.asu.diging.simpleusers.core.service.ITokenService;

@Controller
public class RequestPasswordResetController extends SimpleUserBaseController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ITokenService tokenService;
    
    @Autowired
    private ConfigurationProvider configProvider;
    
    @Override
    protected String getMappingPath() {
        return null;
    }

    @Override
    protected ModelAndView handlePost(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        String email = request.getParameter("email");
        logger.debug("Attempting password reset for " + email);
        if (email == null || email.trim().isEmpty()) {
            logger.debug("No email provided.");
            return generateFailureModel("noEmail");
        }
        
        try {
            tokenService.resetPassword(email);
            logger.debug("Password reset initiated.");
        } catch (UserDoesNotExistException | IOException ex) {
            logger.error("A password reset was attempted for " + email, ex);
        }
        
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:" + configProvider.getResetRequestSentEndpoint() + "?success=passwordResetInitiated");
        return model;
    }

    @Override
    protected ModelAndView handleGet(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        throw new MethodNotSupportedException(RequestMethod.GET);
    }

    @Override
    protected String getFailureViewName() {
        return "redirect:/";
    }

}
