package pl.pszczolkowski.chat.server.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Session;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS) 
public class SecurityContextImpl implements SecurityContext {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public ActiveUser getAuthenticatedUser() {
        return this.session.getUser();
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void clearSession() {
        session = null;
    }

    @Override
    public boolean isUserAuthenticated() {
        return session != null;
    }

}
