package pl.pszczolkowski.chat.server.config.xmlrpc;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.XmlRpcServletServer;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import pl.pszczolkowski.chat.server.service.ApisContainer;

public class XmlRpcServiceExporter implements HttpRequestHandler {

    private final ApisContainer apisContainer;
    private final XmlRpcServletServer server;

    public XmlRpcServiceExporter(RequestProcessorFactoryFactory requestProcessorFactoryFactory,
            ApisContainer apisContainer, XmlRpcAuthenticationHandler xmlRpcAuthenticationHandler)
            throws XmlRpcException {
        this.apisContainer = apisContainer;
        server = new XmlRpcServletServer();

        PropertyHandlerMapping handlerMapping = new PropertyHandlerMapping();
        handlerMapping.setRequestProcessorFactoryFactory(requestProcessorFactoryFactory);
        handlerMapping.setAuthenticationHandler(xmlRpcAuthenticationHandler);

        registerApisAsHandlers(handlerMapping);

        server.setHandlerMapping(handlerMapping);

        XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
        config.setEnabledForExtensions(true);
        config.setEnabledForExceptions(true);
        server.setConfig(config);
    }

    private void registerApisAsHandlers(PropertyHandlerMapping handlerMapping) throws XmlRpcException {
        List<Object> apis = apisContainer.getAll();

        for (Object api : apis) {
            for (Class<?> implementedInterface : api.getClass().getInterfaces()) {
                handlerMapping.addHandler(implementedInterface.getName(), api.getClass());
            }
        }
    }

    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"POST".equals(request.getMethod())) {
            throw new HttpRequestMethodNotSupportedException(request.getMethod(), new String[] { "POST" },
                    "XmlRpcServiceExporter only supports POST requests");
        }
        
        server.execute(request, response);
    }

}
