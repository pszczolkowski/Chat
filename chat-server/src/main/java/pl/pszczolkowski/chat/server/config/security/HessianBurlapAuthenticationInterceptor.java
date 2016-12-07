package pl.pszczolkowski.chat.server.config.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import pl.pszczolkowski.chat.server.annotation.AccessibleByAnonymousUsers;
import pl.pszczolkowski.chat.server.service.ApisContainer;
import pl.pszczolkowski.chat.server.service.Security;

@Component
public class HessianBurlapAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private final Security security;

    private final List<String> anonymouslyAccessibleApiNames;
    
    @Autowired
    public HessianBurlapAuthenticationInterceptor(Security kernel, ApisContainer apisContainer) {
        this.security = kernel;
        this.anonymouslyAccessibleApiNames = anonymouslyAccessibleApiNames(apisContainer);
    }

    private List<String> anonymouslyAccessibleApiNames(ApisContainer apisContainer) {
        List<String> anonymouslyAccessibleApiNames = new ArrayList<>();
        
        List<Object> apis = apisContainer.getAll();
        for (Object api : apis) {
            if (api.getClass().isAnnotationPresent(AccessibleByAnonymousUsers.class)) {
                anonymouslyAccessibleApiNames.add(api.getClass().getInterfaces()[0].getSimpleName());
            }
        }
        
        return anonymouslyAccessibleApiNames;
    }

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {
        boolean isAuthenticationSuccessful = authenticate(request);

        if (isAuthenticationSuccessful) {
            return true;
        } else if (request.getRequestURI().endsWith("/xmlrpc") || isApiAnonymouslyAccessible(request)) {
            return true;
        } else {
            handleNotAuthorized(request, response, handler);
            return false;
        }
    }

    private boolean authenticate(HttpServletRequest request) {
        Optional<UUID> sessionId = fetchSessionIdFrom(request);
        if (!sessionId.isPresent()) {
            return false;
        }
        
        return security.authenticateUserUsingSessionId(sessionId.get());
    }

    private boolean isApiAnonymouslyAccessible(HttpServletRequest request) {
        if (request.getMethod().equals("GET")) {
            return true;
        }
        
        for (String permittedApiName : anonymouslyAccessibleApiNames) {
            if (request.getRequestURI().endsWith("/" + permittedApiName)) {
                return true;
            }
        }
        
        return false;
    }

    private Optional<UUID> fetchSessionIdFrom(HttpServletRequest request) {
        if (request.getHeader("authorization") == null) {
            return Optional.empty();
        }
        
        try {
            String header = request.getHeader("authorization").substring(6);
            header = new String(Base64.getDecoder().decode(header));
            String sessionId = header.split(":")[1];
            return Optional.of(UUID.fromString(sessionId));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    protected void handleNotAuthorized(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
