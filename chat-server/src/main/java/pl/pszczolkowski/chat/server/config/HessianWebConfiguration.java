package pl.pszczolkowski.chat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.caucho.HessianServiceExporter;

import pl.pszczolkowski.chat.shared.api.AccountApi;
import pl.pszczolkowski.chat.shared.api.ChatApi;
import pl.pszczolkowski.chat.shared.api.RoomApi;

@Configuration
public class HessianWebConfiguration {

    @Bean(name = "/hessian/AccountApi")
    public HessianServiceExporter accountApi(AccountApi accountApi) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(accountApi);
        exporter.setServiceInterface(AccountApi.class);
        return exporter;
    }

    @Bean(name = "/hessian/ChatApi")
    public HessianServiceExporter chatApi(ChatApi chatApi) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(chatApi);
        exporter.setServiceInterface(ChatApi.class);
        return exporter;
    }

    @Bean(name = "/hessian/RoomApi")
    public HessianServiceExporter roomApi(RoomApi roomApi) {
        HessianServiceExporter exporter = new HessianServiceExporter();
        exporter.setService(roomApi);
        exporter.setServiceInterface(RoomApi.class);
        return exporter;
    }

}
