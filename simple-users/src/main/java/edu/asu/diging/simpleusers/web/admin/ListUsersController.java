package edu.asu.diging.simpleusers.web.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.asu.diging.simpleusers.core.config.ConfigurationProvider;
import edu.asu.diging.simpleusers.core.service.IUserManager;

@Controller
public class ListUsersController {
    
    @Autowired
    private ConfigurationProvider configProvider;

    @Autowired
    private IUserManager userManager;

    @RequestMapping("/admin/user/list")
    public String list(Model model) {
        model.addAttribute("users", userManager.findAll());
        return configProvider.getUserListView();
    }
}
