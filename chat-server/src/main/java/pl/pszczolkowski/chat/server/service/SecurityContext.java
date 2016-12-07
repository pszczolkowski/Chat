package pl.pszczolkowski.chat.server.service;

import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Session;

/**
 * Holds session for particular request. 
 * Implementing bean has to be request-scoped
 */
public interface SecurityContext {

    void setSession(Session session);
    
    ActiveUser getAuthenticatedUser();

    Session getSession();

    void clearSession();

    boolean isUserAuthenticated();
    
}
