package edu.asu.diging.simpleusers.core.config;

public interface SimpleUsers {

    String getRegisterView();

    String getUserListView();

    SimpleUsers registerView(String registerView);

    SimpleUsers userListView(String userListView);

    String getRegisterSuccessRedirect();

    SimpleUsers registerSuccessRedirect(String successRedirect);

    SimpleUsers usersEndpointPrefix(String listUsersEndpoint);

    String getUsersEndpointPrefix();

    long getTokenExpirationPeriod();

    SimpleUsers tokenExpirationPeriod(long min);

    String getEmailServerHost();

    String getEmailServerPort();

    String getEmailPassword();

    String getEmailUsername();

    SimpleUsers emailServerHost(String host);

    SimpleUsers emailServerPort(String port);

    SimpleUsers emailPassword(String password);

    SimpleUsers emailUsername(String username);

    boolean isEmailDebug();

    boolean isEmailStartTlsEnable();

    boolean isEmailAuthentication();

    String getEmailProtocol();

    SimpleUsers emailDebug(boolean debug);

    SimpleUsers emailStartTlsEnable(boolean startTlsEnable);

    SimpleUsers emailAuthentication(boolean authentication);

    SimpleUsers emailProtocol(String protocol);

    String getInstanceUrl();

    SimpleUsers instanceUrl(String url);

    String getEmailFrom();

    SimpleUsers emailFrom(String from);

    String getAppName();

    SimpleUsers appName(String appName);

    String getResetPasswordEndpoint();

    SimpleUsers resetPasswordEndpoint(String resetPasswordEndpoint);

    String getEmailSubject();

    String getEmailBody();

    SimpleUsers emailSubject(String subject);

    SimpleUsers emailBody(String body);

    String getChangePasswordEndpoint();

    SimpleUsers changePasswordEndpoint(String endpoint);

    String getChangePasswordView();

    SimpleUsers changePasswordView(String view);

    String getResetRequestSentEndpoint();

    SimpleUsers resetRequestSentEndpoint(String resetRequestSent);

    String getResetPasswordInitiatedEndpoint();

    SimpleUsers resetPasswordInitiatedEndpoint(String endpoint);

    
}
