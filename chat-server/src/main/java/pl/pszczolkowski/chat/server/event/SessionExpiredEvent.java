package pl.pszczolkowski.chat.server.event;

import org.springframework.context.ApplicationEvent;

import pl.pszczolkowski.chat.server.model.Session;

public class SessionExpiredEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;
    
    private Session session;

    public SessionExpiredEvent(Session session) {
        super(session);
        this.session = session;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public Session getSession() {
        return session;
    }

}
