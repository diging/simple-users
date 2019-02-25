package edu.asu.diging.simpleusers.core.factory;

import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.web.forms.UserForm;

public interface IUserFactory {

    IUser createUser(UserForm userForm);

    IUser createUser(String username, String password, String role, boolean enabled);

}