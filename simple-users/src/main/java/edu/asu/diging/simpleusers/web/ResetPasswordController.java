package edu.asu.diging.simpleusers.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.exceptions.InvalidTokenException;
import edu.asu.diging.simpleusers.core.exceptions.MethodNotSupportedException;
import edu.asu.diging.simpleusers.core.exceptions.TokenExpiredException;
import edu.asu.diging.simpleusers.core.service.ITokenService;

@Controller
public class ResetPasswordController extends SimpleUserBaseController {

    @Autowired
    private ConfigurationProvider configProvider;
    
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
        throw new MethodNotSupportedException(RequestMethod.POST);
    }

    @Override
    protected ModelAndView handleGet(HttpServletRequest request, HttpServletResponse response)
            throws MethodNotSupportedException, Exception {
        String username = request.getParameter("user");
        String token = request.getParameter("token");
        try {
            tokenService.validateToken(token, username);
        } catch(InvalidTokenException | TokenExpiredException ex) {
            return generateFailureModel("invalidToken");
            
        }
        
        ModelAndView model = new ModelAndView();
        model.setViewName("redirect:" + configProvider.getChangePasswordEndpoint());
        return model;
    }

    @Override
    protected String getFailureViewName() {
        return "redirect:/";
    }
    
    
}
