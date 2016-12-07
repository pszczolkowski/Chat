package pl.pszczolkowski.chat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.BurlapServiceExporter;

import pl.pszczolkowski.chat.shared.api.AccountApi;
import pl.pszczolkowski.chat.shared.api.ChatApi;
import pl.pszczolkowski.chat.shared.api.RoomApi;

@Configuration
@SuppressWarnings("deprecation")
public class BurlapWebConfiguration {

    @Bean(name = "/burlap/AccountApi")
    public BurlapServiceExporter accountApi(AccountApi accountApi) {
        BurlapServiceExporter exporter = new BurlapServiceExporter();
        exporter.setService(accountApi);
        exporter.setServiceInterface(AccountApi.class);
        return exporter;
    }
    
    @Bean(name = "/burlap/ChatApi")
    public BurlapServiceExporter chatApi(ChatApi chatApi) {
        BurlapServiceExporter exporter = new BurlapServiceExporter();
        exporter.setService(chatApi);
        exporter.setServiceInterface(ChatApi.class);
        return exporter;
    }
    
    @Bean(name = "/burlap/RoomApi")
    public BurlapServiceExporter roomApi(RoomApi roomApi) {
        BurlapServiceExporter exporter = new BurlapServiceExporter();
        exporter.setService(roomApi);
        exporter.setServiceInterface(RoomApi.class);
        return exporter;
    }
    
}
