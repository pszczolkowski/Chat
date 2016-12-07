package pl.pszczolkowski.chat.server.config.xmlrpc;

import static java.util.stream.Collectors.toList;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.common.XmlRpcHttpRequestConfig;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.pszczolkowski.chat.server.annotation.AccessibleByAnonymousUsers;
import pl.pszczolkowski.chat.server.service.ApisContainer;
import pl.pszczolkowski.chat.server.service.Security;

@Component
public class XmlRpcAuthenticationHandler implements AbstractReflectiveHandlerMapping.AuthenticationHandler {

    private final Security security;
    private final List<String> anonymouslyAccessibleMethodNames;

    @Autowired
    public XmlRpcAuthenticationHandler(Security security, ApisContainer apisContainer) {
        this.security = security;
        this.anonymouslyAccessibleMethodNames = fetchAnonymouslyAccessibleMethodNames(apisContainer);
    }

    private List<String> fetchAnonymouslyAccessibleMethodNames(ApisContainer apisContainer) {
        List<String> methodNames = new ArrayList<>();
        
        List<Object> anonymouslyAccessibleApis = apisContainer.getAll()
                .stream()
                .filter(api -> api.getClass().isAnnotationPresent(AccessibleByAnonymousUsers.class))
                .collect(toList());
        
        for (Object api : anonymouslyAccessibleApis) {
            methodNames.addAll(getPublicMethodNamesOf(api));
        }
        
        return methodNames;
    }

    private Collection<? extends String> getPublicMethodNamesOf(Object api) {
        String className = api.getClass().getInterfaces()[0].getName();
        List<String> methods = new ArrayList<>();
        
        for (Method method : api.getClass().getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                methods.add(className + "." + method.getName());
            }
        }
        
        return methods;
    }

    @Override
    public boolean isAuthorized(XmlRpcRequest request) throws XmlRpcException {
        if (isMethodAnonymouslyAccessible(request.getMethodName())) {
            return true;
        }

        UUID sessionId = fetchSessionIdFromRequestHeader(request);
        boolean isAuthenticationSuccessful = security.authenticateUserUsingSessionId(sessionId);
        
        return isAuthenticationSuccessful;
    }

    private boolean isMethodAnonymouslyAccessible(String methodName) {
        for (String anonymouslyAccessibleMethodName : anonymouslyAccessibleMethodNames) {
            if (methodName.equals(anonymouslyAccessibleMethodName)) {
                return true;
            }
        }
        
        return false;
    }
    
    private UUID fetchSessionIdFromRequestHeader(XmlRpcRequest request) {
        XmlRpcHttpRequestConfig config = (XmlRpcHttpRequestConfig) request.getConfig();
        UUID sessionId = UUID.fromString(config.getBasicPassword());
        return sessionId;
    }

}
