package edu.asu.diging.simpleusers.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.exceptions.InvalidTokenException;
import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;
import edu.asu.diging.simpleusers.core.service.ITokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class ResetPasswordInitiatedController extends SimpleUserBaseController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConfigurationProvider configProvider;
    
    @Autowired
    private ITokenService tokenService;

    @Override
    protected String getMappingPath() {
        return null;
    }

    @Override
    protected ModelAndView handlePost(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        throw new MethodNotSupportedException(RequestMethod.POST);
    }

    @Override
    protected ModelAndView handleGet(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        String username = request.getParameter("user");
        String token = request.getParameter("token");
        if (username == null || username.trim().isEmpty() || token == null || token.trim().isEmpty()) {
            logger.info("No username or no token provided.");
            return generateFailureModel("invalidToken");
        }
        try {
            tokenService.validateToken(token, username);
        } catch(InvalidTokenException | TokenExpiredException ex) {
            logger.warn("Token failed to validate.", ex);
            return generateFailureModel("invalidToken");
            
        }
        logger.debug("Allow password change for user " + username);
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:" + configProvider.getChangePasswordEndpoint());
        return model;
    }

    @Override
    protected String getFailureViewName() {
        return "redirect:/";
    }
    
    
}
