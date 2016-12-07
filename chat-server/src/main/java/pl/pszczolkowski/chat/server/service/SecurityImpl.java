package pl.pszczolkowski.chat.server.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import pl.pszczolkowski.chat.server.event.SessionExpiredEvent;
import pl.pszczolkowski.chat.server.model.ActiveUser;
import pl.pszczolkowski.chat.server.model.Session;
import pl.pszczolkowski.chat.server.model.User;
import pl.pszczolkowski.chat.shared.Values;

@Service
public class SecurityImpl implements Security {

    private final SecurityContext securityContext;
    private final ApplicationEventPublisher eventPublisher;

    private final List<Session> sessions = new ArrayList<>();

    @Autowired
    public SecurityImpl(SecurityContext securityContext, ApplicationEventPublisher eventPublisher) {
        this.securityContext = securityContext;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public boolean authenticateUserUsingSessionId(UUID sessionId) {
        Session session = findSessionById(sessionId);

        if (session != null) {
            securityContext.setSession(session);
            return true;
        } else {
            return false;
        }
    }

    private Session findSessionById(UUID sessionId) {
        for (Session session : sessions) {
            if (session.getId().equals(sessionId)) {
                return session;
            }
        }

        return null;
    }

    @Override
    public Session authenticate(User user) {
        Optional<Session> sessionOption = findSessionByUser(user);    
    
        if (sessionOption.isPresent()) {
            return sessionOption.get();
        } else {
            Session session = new Session(new ActiveUser(user));
            sessions.add(session);
    
            return session;
        }
    }

    private Optional<Session> findSessionByUser(User user) {
        for (Session session : sessions) {
            if (session.getUser().getId().equals(user.getId())) {
                return Optional.of(session);
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<ActiveUser> findActiveUserByNick(String receiverNick) {
        for (Session session : sessions) {
            if (session.getUser().getNick().equalsIgnoreCase(receiverNick)) {
                return Optional.of(session.getUser());
            }
        }
        
        return Optional.empty();
    }

    @Override
    public void removeExpiredSessions() {
        for (Iterator<Session> iterator = sessions.iterator(); iterator.hasNext();) {
            Session session = iterator.next();
            if (isExpired(session)) {
                eventPublisher.publishEvent(new SessionExpiredEvent(session));
                iterator.remove();
            }
        }
    }

    private boolean isExpired(Session session) {
        return session.getLastActivityTime()
            .plusSeconds(Values.SESSION_EXPIRATION_IN_SECONDS)
            .isBefore(LocalDateTime.now());
    }

    @Override
    public void logout() {
        sessions.remove(securityContext.getSession());
        securityContext.clearSession();
    }

}
