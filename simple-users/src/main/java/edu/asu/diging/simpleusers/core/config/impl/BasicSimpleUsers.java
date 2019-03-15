package edu.asu.diging.simpleusers.core.config.impl;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;

public class BasicSimpleUsers implements SimpleUsers {
    
    private String userListView = "admin/user/list";
    private String registerView = "register";
    private String registerSuccessRedirect = "/";
    
    private String usersEndpointPrefix = "/admin/user/";
    
    @Override
    public SimpleUsers userListView(String userListView) {
        this.userListView = userListView;
        return this;
    }
    
    @Override
    public SimpleUsers registerView(String registerView) {
        this.registerView = registerView;
        return this;
    }
    
    @Override
    public SimpleUsers registerSuccessRedirect(String successRedirect) {
        this.registerSuccessRedirect = successRedirect;
        return this;
    }
    
    @Override
    public String getUserListView() {
        return userListView;
    }
    
    @Override
    public String getRegisterView() {
        return registerView;
    }
    
    @Override
    public String getRegisterSuccessRedirect() {
        return registerSuccessRedirect;
    }

    @Override
    public String getUsersEndpointPrefix() {
        return usersEndpointPrefix;
    }

    @Override
    public SimpleUsers usersEndpointPrefix(String usersEndpointPrefix) {
        this.usersEndpointPrefix = usersEndpointPrefix;
        return this;
    }

}
