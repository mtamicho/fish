package com.example.demo;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.protocol.core.methods.response.Transaction;
import io.reactivex.disposables.Disposable;

import java.io.IOException;
import java.math.BigInteger;

public class MempoolWSSTransactionListener {

    public static void main(String[] args) throws IOException {

        //让Java程序走代理，需要在请求前设置代理参数
//        String proxyHost = "127.0.0.1";
//        String proxyPort = "7890"; // Clash默认的Socks5和http代理端口
//        // 对http请求开启代理
//        System.setProperty("http.proxyHost", proxyHost);
//        System.setProperty("http.proxyPort", proxyPort);
//        // 对https也开启代理
//        System.setProperty("https.proxyHost", proxyHost);
//        System.setProperty("https.proxyPort", proxyPort);
//
//        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");
//
//
//        // 配置代理服务器
//        System.setProperty("socksProxyHost", proxyHost);
//        System.setProperty("socksProxyPort", proxyPort);

        // 使用QuickNode提供的WebSocket地址
        String webSocketUrl = "wss://eth-mainnet.g.alchemy.com/v2/";

        WebSocketService webSocketService = new WebSocketService(webSocketUrl, true);
        webSocketService.connect();

        // 创建Web3j实例
        Web3j web3j = Web3j.build(webSocketService);

        // 订阅pending交易通知
        Disposable pendingTransactionSubscription = web3j.pendingTransactionFlowable().subscribe(notification -> {
            String transactionHash = notification.getHash();
            web3j.ethGetTransactionByHash(transactionHash).sendAsync().thenAccept(transactionResponse -> {
                Transaction transaction = transactionResponse.getTransaction().orElse(null);
                if (transaction != null) {
                    String from = transaction.getFrom();
                    String to = transaction.getTo();
                    BigInteger value = transaction.getValue();
                    BigInteger gas = transaction.getGas();
                    BigInteger gasPrice = transaction.getGasPrice();
                    System.out.println("New pending transaction: " + gasPrice + " : " + gas);
                    System.out.println("Transaction Hash: " + transactionHash);
                    System.out.println("From: " + from);
                    System.out.println("To: " + to);
                    System.out.println("Value: " + value);
                    // 处理交易逻辑
                }
            }).exceptionally(throwable -> {
                System.out.println("Failed to fetch transaction: " + throwable.getMessage());
                return null;
            });
        });

        // 阻塞，等待事件通知
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("dis pending");

        // 取消订阅并断开连接
        pendingTransactionSubscription.dispose();
        webSocketService.close();
    }
}
