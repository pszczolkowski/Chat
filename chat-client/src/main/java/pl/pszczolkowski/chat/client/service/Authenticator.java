package pl.pszczolkowski.chat.client.service;

import java.util.UUID;

import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caucho.burlap.client.BurlapProxyFactory;
import com.caucho.hessian.client.HessianProxyFactory;

import pl.pszczolkowski.chat.shared.exception.BadCredentialsException;
import pl.pszczolkowski.chat.shared.model.AuthenticationResponse;


@Service
public class Authenticator {

    private final ApiWrapper apiWrapper;
    private final HessianProxyFactory hessianProxyFactory;
    private final BurlapProxyFactory burlapProxyFactory;
    private final XmlRpcClientConfigImpl xmlRpcClientConfig;

    @Autowired
    public Authenticator(ApiWrapper apiWrapper, HessianProxyFactory hessianProxyFactory,
            BurlapProxyFactory burlapProxyFactory, XmlRpcClientConfigImpl xmlRpcClientConfig) {
        this.apiWrapper = apiWrapper;
        this.hessianProxyFactory = hessianProxyFactory;
        this.burlapProxyFactory = burlapProxyFactory;
        this.xmlRpcClientConfig = xmlRpcClientConfig;
    }

    public void authenticateWithCredentials(String nick, String password) throws BadCredentialsException {
        AuthenticationResponse response = apiWrapper.getAccountApi().authenticate(nick, password);
        UUID sessionId = response.getSessionId();

        hessianProxyFactory.setPassword(sessionId.toString());
        burlapProxyFactory.setPassword(sessionId.toString());
        xmlRpcClientConfig.setBasicUserName("user");
        xmlRpcClientConfig.setBasicPassword(sessionId.toString());
    }

}
