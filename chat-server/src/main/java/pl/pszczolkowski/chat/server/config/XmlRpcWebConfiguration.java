package pl.pszczolkowski.chat.server.config;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.pszczolkowski.chat.server.config.xmlrpc.XmlRpcAuthenticationHandler;
import pl.pszczolkowski.chat.server.config.xmlrpc.XmlRpcServiceExporter;
import pl.pszczolkowski.chat.server.service.ApisContainer;

@Configuration
public class XmlRpcWebConfiguration {

    @Autowired
    private RequestProcessorFactoryFactory requestProcessorFactoryFactory;
    
    @Autowired
    private ApisContainer apisContainer;
    
    @Autowired
    private XmlRpcAuthenticationHandler xmlRpcAuthenticationHandler;
    
    @Bean(name = "/xmlrpc")
    public XmlRpcServiceExporter xmlRpcServlet() throws XmlRpcException {
        return new XmlRpcServiceExporter(requestProcessorFactoryFactory,
                apisContainer, xmlRpcAuthenticationHandler);
    }

}
