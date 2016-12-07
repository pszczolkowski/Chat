package pl.pszczolkowski.chat.client.configuration;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfig;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.util.ClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import pl.pszczolkowski.chat.shared.api.AccountApi;
import pl.pszczolkowski.chat.shared.api.ChatApi;
import pl.pszczolkowski.chat.shared.api.RoomApi;

@Configuration
@PropertySource("classpath:/application.properties")
public class XmlRpcApiConfiguration {

    @Value("${server.url}")
    private String url;

    @Bean
    public XmlRpcClientConfigImpl xmlRpcClientConfig() throws MalformedURLException {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL(url + "xmlrpc"));
        config.setEnabledForExtensions(true);
        config.setEnabledForExceptions(true);

        return config;
    }

    @Bean
    public XmlRpcClient xmlRpcClient(XmlRpcClientConfig config) {
        XmlRpcClient xmlRpcClient = new XmlRpcClient();
        xmlRpcClient.setTransportFactory(new XmlRpcCommonsTransportFactory(xmlRpcClient));
        xmlRpcClient.setConfig(config);

        return xmlRpcClient;
    }

    @Bean
    public ClientFactory xmlRpcClientFactory(XmlRpcClient client) {
        return new ClientFactory(client);
    }
    
    @Bean("xmlRpcAccountApi")
    public AccountApi xmlRpcAccountApi(ClientFactory factory) {
        return (AccountApi) factory.newInstance(AccountApi.class);
    }

    @Bean("xmlRpcRoomApi")
    public RoomApi xmlRpcRoomApi(ClientFactory factory) throws MalformedURLException {
        return (RoomApi) factory.newInstance(RoomApi.class);
    }

    @Bean("xmlRpcChatApi")
    public ChatApi xmlRpcChatApi(ClientFactory factory) throws MalformedURLException {
        return (ChatApi) factory.newInstance(ChatApi.class);
    }
    
}
