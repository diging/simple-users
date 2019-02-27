package edu.asu.diging.simpleusers.web.admin;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.asu.diging.simpleusers.core.service.IUserManager;

@Controller
public class ApproveAccountController {
    
    @Autowired
    private IUserManager userManager;

    @RequestMapping(value="/admin/user/{username}/approve", method=RequestMethod.POST)
    public String approveAccount(@PathVariable("username") String username, Principal principal) {
        userManager.approveAccount(username, principal.getName());
        return "redirect:/admin/user/list";
    }
}