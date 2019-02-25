package edu.asu.diging.simpleusers.web;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.simpleusers.core.exceptions.UserAlreadyExistsException;
import edu.asu.diging.simpleusers.core.factory.IUserFactory;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.service.IUserManager;
import edu.asu.diging.simpleusers.web.forms.UserForm;

@Controller
public class CreateAccountController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private IUserManager userManager;
    
    @Autowired
    private IUserFactory userFactory;

    @RequestMapping(value = "/register", method=RequestMethod.GET)
    public String get(Model model) {
        model.addAttribute("user", new UserForm());
        return "register";
    }
    
    @RequestMapping(value = "/register", method=RequestMethod.POST)
    public String post(@Valid @ModelAttribute("user") UserForm userForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("user", userForm);
            return "register";
        }
        
        IUser user = userFactory.createUser(userForm);
        try {
            userManager.create(user);
        } catch (UserAlreadyExistsException e) {
            logger.error("User could not be created. Username already in use.");
            model.addAttribute("user", userForm);
            result.rejectValue("username", "username", "Username is already in use.");
            return "register";
        }
        
        return "redirect:/ ";
    }
}