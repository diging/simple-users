package edu.asu.diging.simpleusers.core.config;

public interface SimpleUsers {

    String getRegisterView();

    String getUserListView();

    SimpleUsers registerView(String registerView);

    SimpleUsers userListView(String userListView);

    String getRegisterSuccessRedirect();

    SimpleUsers registerSuccessRedirect(String successRedirect);

    
}
