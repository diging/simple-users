package edu.asu.diging.simpleusers.core.config;

import edu.asu.diging.simpleusers.core.config.impl.ConfigurationProviderImpl;

public interface ConfigurationProvider {
    
    /**
     * Helper method, if used without component scan or XML bean declaration.
     * @return
     */
    public static ConfigurationProvider build() {
        return new ConfigurationProviderImpl();
    }

    String getUserListView();

    String getRegisterView();

}