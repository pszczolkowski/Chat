package pl.pszczolkowski.chat.client.configuration;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.caucho.burlap.client.BurlapProxyFactory;

import pl.pszczolkowski.chat.shared.api.AccountApi;
import pl.pszczolkowski.chat.shared.api.ChatApi;
import pl.pszczolkowski.chat.shared.api.RoomApi;

@Configuration
@PropertySource("classpath:/application.properties")
public class BurlapConfiguration {

    @Value("${server.url}")
    private String url;

    @Bean
    public BurlapProxyFactory burlapProxyFactory() {
        BurlapProxyFactory factory = new BurlapProxyFactory();
        factory.setUser("dummy");

        return factory;
    }

    @Bean("burlapAccountApi")
    public AccountApi burlapAccountApi(BurlapProxyFactory factory) throws MalformedURLException {
        return (AccountApi) factory.create(AccountApi.class, url + "burlap/AccountApi");
    }

    @Bean("burlapRoomApi")
    public RoomApi burlapRoomApi(BurlapProxyFactory factory) throws MalformedURLException {
        return (RoomApi) factory.create(RoomApi.class, url + "burlap/RoomApi");
    }

    @Bean("burlapChatApi")
    public ChatApi burlapChatApi(BurlapProxyFactory factory) throws MalformedURLException {
        return (ChatApi) factory.create(ChatApi.class, url + "burlap/ChatApi");
    }

}
