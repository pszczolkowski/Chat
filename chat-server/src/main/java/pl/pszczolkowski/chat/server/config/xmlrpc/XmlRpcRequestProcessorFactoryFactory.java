package pl.pszczolkowski.chat.server.config.xmlrpc;

import java.util.Optional;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.pszczolkowski.chat.server.service.ApisContainer;

@Service
public class XmlRpcRequestProcessorFactoryFactory implements RequestProcessorFactoryFactory {

    private ApisContainer apisContainer;

    @Autowired
    public XmlRpcRequestProcessorFactoryFactory(ApisContainer apisContainer) {
        this.apisContainer = apisContainer;
    }

    @SuppressWarnings("rawtypes")
    public RequestProcessorFactory getRequestProcessorFactory(Class clazz) throws XmlRpcException {
        Optional<Object> api = apisContainer
                .getAll()
                .stream()
                .filter(o -> o.getClass().equals(clazz))
                .findFirst();

        if (api.isPresent()) {
            return new XmlRpcRequestProcessorFactory(api.get());
        } else {
            throw new IllegalArgumentException("no registered handler for class <" + clazz.getName()
                    + ">. Did you forget to use @Api annotation?");
        }
    }

}
