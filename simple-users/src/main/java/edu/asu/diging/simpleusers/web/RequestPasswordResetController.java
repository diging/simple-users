package edu.asu.diging.simpleusers.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;
import edu.asu.diging.simpleusers.core.service.ITokenService;

public class RequestPasswordResetController extends SimpleUserBaseController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ITokenService tokenService;
    
    @Override
    protected String getMappingPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ModelAndView handlePost(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        String email = request.getParameter("email");
        if (email == null || email.trim().isEmpty()) {
            return generateFailureModel("noEmail");
        }
        
        try {
            tokenService.resetPassword(email);
        } catch (UserDoesNotExistException | IOException ex) {
            logger.error("A password reset was attempted for " + email, ex);
        }
        
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:/" + "?success=passwordResetInitiated");
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
