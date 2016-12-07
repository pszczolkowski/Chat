package pl.pszczolkowski.chat.client.service;

import static pl.pszczolkowski.chat.client.shared.Protocol.HESSIAN;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pl.pszczolkowski.chat.client.shared.Protocol;
import pl.pszczolkowski.chat.shared.api.AccountApi;
import pl.pszczolkowski.chat.shared.api.ChatApi;
import pl.pszczolkowski.chat.shared.api.RoomApi;

@Service
public class ApiWrapper {

    private Protocol currentProtocol = HESSIAN;

    private final AccountApi xmlRpcAccountApi;
    private final ChatApi xmlRpcChatApi;
    private final RoomApi xmlRpcRoomApi;
    private final AccountApi hessianAccountApi;
    private final ChatApi hessianChatApi;
    private final RoomApi hessianRoomApi;
    private final AccountApi burlapAccountApi;
    private final ChatApi burlapChatApi;
    private final RoomApi burlapRoomApi;

    @Autowired
    public ApiWrapper(@Qualifier("xmlRpcAccountApi") AccountApi xmlRpcAccountApi,
            @Qualifier("xmlRpcChatApi") ChatApi xmlRpcChatApi, @Qualifier("xmlRpcRoomApi") RoomApi xmlRpcRoomApi,
            @Qualifier("hessianAccountApi") AccountApi hessianAccountApi,
            @Qualifier("hessianChatApi") ChatApi hessianChatApi, @Qualifier("hessianRoomApi") RoomApi hessianRoomApi,
            @Qualifier("burlapAccountApi") AccountApi burlapAccountApi,
            @Qualifier("burlapChatApi") ChatApi burlapChatApi, @Qualifier("burlapRoomApi") RoomApi burlapRoomApi) {
        this.xmlRpcAccountApi = xmlRpcAccountApi;
        this.xmlRpcChatApi = xmlRpcChatApi;
        this.xmlRpcRoomApi = xmlRpcRoomApi;
        this.hessianAccountApi = hessianAccountApi;
        this.hessianChatApi = hessianChatApi;
        this.hessianRoomApi = hessianRoomApi;
        this.burlapAccountApi = burlapAccountApi;
        this.burlapChatApi = burlapChatApi;
        this.burlapRoomApi = burlapRoomApi;
    }
    
    public void setCurrentProtocol(Protocol protocol) {
        this.currentProtocol = protocol;
    }

    public AccountApi getAccountApi() {
        switch (currentProtocol) {
            case XML_RPC:
                return xmlRpcAccountApi;
            case HESSIAN:
                return hessianAccountApi;
            case BURLAP:
                return burlapAccountApi;
            default:
                throw new IllegalArgumentException("unsupported protocol <" + currentProtocol + ">");
        }
    }

    public ChatApi getChatApi() {
        switch (currentProtocol) {
            case XML_RPC:
                return xmlRpcChatApi;
            case HESSIAN:
                return hessianChatApi;
            case BURLAP:
                return burlapChatApi;
            default:
                throw new IllegalArgumentException("unsupported protocol <" + currentProtocol + ">");
        }
    }

    public RoomApi getRoomApi() {
        switch (currentProtocol) {
            case XML_RPC:
                return xmlRpcRoomApi;
            case HESSIAN:
                return hessianRoomApi;
            case BURLAP:
                return burlapRoomApi;
            default:
                throw new IllegalArgumentException("unsupported protocol <" + currentProtocol + ">");
        }
    }

    public Protocol getCurrentProtocol() {
        return this.currentProtocol;
    }

}
