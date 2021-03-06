package edu.asu.diging.simpleusers.core.service;

import java.util.List;

import edu.asu.diging.simpleusers.core.exceptions.UserAlreadyExistsException;
import edu.asu.diging.simpleusers.core.exceptions.UserDoesNotExistException;
import edu.asu.diging.simpleusers.core.model.IUser;
import edu.asu.diging.simpleusers.core.model.Role;

public interface IUserManager {

    void create(IUser user) throws UserAlreadyExistsException;

    IUser findByUsername(String username);

    List<IUser> findAll();

    void approveAccount(String username, String approver);

    /**
     * Adds given role to a user. 
     * @param username Username of user to be changed.
     * @param initiator Username of user initiating the changne.
     * @param role Name of role to be added. Use roles defined in {@link Role}.
     */
    void addRole(String username, String initiator, String role);

    void removeRole(String username, String initiator, String role);

    void disableUser(String username, String initiator);

    IUser findByEmail(String email);

    void changePassword(IUser user, String password) throws UserDoesNotExistException;

}