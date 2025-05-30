package edu.asu.diging.simpleusers.web.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.service.IUserManager;

@Controller
public class ListUsersController extends AbstractController {
    
    public final static String REQUEST_MAPPING_PATH = "list";

    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private IUserManager userManager;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1) throws Exception {
        ModelAndView model = new ModelAndView();
        model.addObject("users", userManager.findAll());
        model.setViewName(configProvider.getUserListView());
        return model;
    }
}
