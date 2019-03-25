package edu.asu.diging.simpleusers.core.config.impl;

import edu.asu.diging.simpleusers.core.config.SimpleUsers;

public class BasicSimpleUsers implements SimpleUsers {
    
    private String userListView = "admin/user/list";
    private String registerView = "register";
    private String registerSuccessRedirect = "/";
    
    private String usersEndpointPrefix = "/admin/user/";
    private String resetPasswordEndpoint = "/password/reset";
    
    private long tokenExpirationPeriod = 1440;
    
    private String emailUsername;
    private String emailPassword;
    private String emailServerPort;
    private String emailServerHost;
    
    private String emailProtocol = "smtp";
    private boolean emailAuthentication = true;
    private boolean emailStartTlsEnable = true;
    private boolean emailDebug = false;
    private String emailFrom;
    
    private String instanceUrl;
    private String appName = "Web Application";
    
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
    public SimpleUsers usersEndpointPrefix(String usersEndpointPrefix) {
        this.usersEndpointPrefix = usersEndpointPrefix;
        return this;
    }
    
    @Override
    public SimpleUsers resetPasswordEndpoint(String resetPasswordEndpoint) {
        this.resetPasswordEndpoint = resetPasswordEndpoint;
        return this;
    }
    
    @Override
    public SimpleUsers tokenExpirationPeriod(long min) {
        this.tokenExpirationPeriod = min;
        return this;
    }
    
    @Override
    public SimpleUsers emailUsername(String username) {
        this.emailUsername = username;
        return this;
    }
    
    @Override
    public SimpleUsers emailPassword(String password) {
        this.emailPassword = password;
        return this;
    }
    
    @Override
    public SimpleUsers emailServerPort(String port) {
        this.emailServerPort = port;
        return this;
    }
    
    @Override
    public SimpleUsers emailServerHost(String host) {
        this.emailServerHost = host;
        return this;
    }
    
    @Override
    public SimpleUsers emailProtocol(String protocol) {
        this.emailProtocol = protocol;
        return this;
    }
    
    @Override
    public SimpleUsers emailAuthentication(boolean authentication) {
        this.emailAuthentication = authentication;
        return this;
    }
    
    @Override
    public SimpleUsers emailStartTlsEnable(boolean startTlsEnable) {
        this.emailStartTlsEnable = startTlsEnable;
        return this;
    }
    
    @Override
    public SimpleUsers emailDebug(boolean debug) {
        this.emailDebug = debug;
        return this;
    }
    
    @Override
    public SimpleUsers emailFrom(String from) {
        this.emailFrom = from;
        return this;
    }
    
    @Override
    public SimpleUsers instanceUrl(String url) {
        this.instanceUrl = url;
        return this;
    }
    
    @Override
    public SimpleUsers appName(String appName) {
        this.appName = appName;
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
    public String getResetPasswordEndpoint() {
        return this.resetPasswordEndpoint;
    }
    
    @Override
    public long getTokenExpirationPeriod() {
        return this.tokenExpirationPeriod;
    }

    @Override
    public String getEmailUsername() {
        return emailUsername;
    }

    @Override
    public String getEmailPassword() {
        return emailPassword;
    }

    @Override
    public String getEmailServerPort() {
        return emailServerPort;
    }

    @Override
    public String getEmailServerHost() {
        return emailServerHost;
    }

    @Override
    public String getEmailProtocol() {
        return emailProtocol;
    }

    @Override
    public boolean isEmailAuthentication() {
        return emailAuthentication;
    }

    @Override
    public boolean isEmailStartTlsEnable() {
        return emailStartTlsEnable;
    }

    @Override
    public boolean isEmailDebug() {
        return emailDebug;
    }
    
    @Override
    public String getEmailFrom() {
        return emailFrom;
    }
    
    @Override
    public String getInstanceUrl() {
        return this.instanceUrl;
    }
    
    @Override
    public String getAppName() {
        return this.appName;
    }
}
