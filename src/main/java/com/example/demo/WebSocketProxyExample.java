package com.example.demo;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.web3j.protocol.websocket.WebSocketService;

public class WebSocketProxyExample {

    public static void main(String[] args) throws URISyntaxException {
        String webSocketUrl = "wss://indulgent-virulent-sun.bsc.discover.quiknode.pro//";
        String proxyHost = "127.0.0.1";
        int proxyPort = 7890; // Clash默认的Socks5代理端口

        // 配置代理服务器
//        System.setProperty("socksProxyHost", proxyHost);
//        System.setProperty("socksProxyPort", String.valueOf(proxyPort));

        // 连接到QuickNode的WebSocket服务
        WebSocketService webSocketService = new WebSocketService(webSocketUrl, false);
        try {
            webSocketService.connect();
            System.out.println("connect success");
        } catch (ConnectException e) {
            throw new RuntimeException(e);
        }

        Draft draft = new Draft_6455();
        WebSocketClient client = new WebSocketClient(new URI(webSocketUrl), draft) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                System.out.println("Connected to WebSocket");
            }

            @Override
            public void onMessage(String message) {
                System.out.println("Received message: " + message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                System.out.println("WebSocket closed");
            }

            @Override
            public void onError(Exception ex) {
                System.out.println("WebSocket error: " + ex.getMessage());
            }
        };

        // 连接到WebSocket服务
        client.connect();
    }
}

