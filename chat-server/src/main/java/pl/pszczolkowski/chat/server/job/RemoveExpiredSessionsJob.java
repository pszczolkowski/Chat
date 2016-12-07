package pl.pszczolkowski.chat.server.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.server.service.Security;
import pl.pszczolkowski.chat.shared.Values;

@Component
public class RemoveExpiredSessionsJob {

    private final Security security;
    
    @Autowired
    public RemoveExpiredSessionsJob(Security security) {
        this.security = security;
    }

    @Scheduled(fixedRate = Values.REMOVE_IDLE_SESSIONS_INTERVAL)
    public void removeExpiredSessions() {
        security.removeExpiredSessions();
    }
    
}
