package pl.pszczolkowski.chat.client.configuration;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.caucho.hessian.client.HessianProxyFactory;

import pl.pszczolkowski.chat.shared.api.AccountApi;
import pl.pszczolkowski.chat.shared.api.ChatApi;
import pl.pszczolkowski.chat.shared.api.RoomApi;

@Configuration
@PropertySource("classpath:/application.properties")
public class HessianApiConfiguration {

    @Value("${server.url}")
    private String url;

    @Bean
    public HessianProxyFactory hessianProxyFactory() {
        HessianProxyFactory factory = new HessianProxyFactory();
        factory.setUser("dummy");

        return factory;
    }

    @Bean("hessianAccountApi")
    public AccountApi hessianAccountApi(HessianProxyFactory factory) throws MalformedURLException {
        return (AccountApi) factory.create(AccountApi.class, url + "hessian/AccountApi");
    }

    @Bean("hessianRoomApi")
    public RoomApi hessianRoomApi(HessianProxyFactory factory) throws MalformedURLException {
        return (RoomApi) factory.create(RoomApi.class, url + "hessian/RoomApi");
    }

    @Bean("hessianChatApi")
    public ChatApi hessianChatApi(HessianProxyFactory factory) throws MalformedURLException {
        return (ChatApi) factory.create(ChatApi.class, url + "hessian/ChatApi");
    }

}
