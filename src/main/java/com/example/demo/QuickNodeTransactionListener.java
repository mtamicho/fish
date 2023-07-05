package com.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;

public class QuickNodeTransactionListener {

    public static void main(String[] args) throws IOException {
        String quickNodeUrl = "https://indulgent-virulent-sun.bsc.discover.quiknode.pro/6595941518f4592e422b3c9373ea980076ca0b92/";
        String apiKey = "";

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(quickNodeUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", apiKey)
                //.post(RequestBody.create(null, ""))
                .build();

        WebSocketListener listener = new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                System.out.println("Connected to QuickNode WebSocket");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println(text);
//                JSONObject jsonObject = new JSONObject(text);
//                JSONArray transactions = jsonObject.getJSONArray("transactions");
//                System.out.println("Received " + transactions.length() + " new transactions:");
//                for (int i = 0; i < transactions.length(); i++) {
//                    JSONObject transaction = transactions.getJSONObject(i);
//                    System.out.println("Transaction Hash: " + transaction.getString("hash"));
//                    // 处理其他交易数据
//                }
            }

            @Override
            public void onClosing(WebSocket webSocket, int code, String reason) {
                System.out.println("WebSocket closing: " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                System.out.println("WebSocket failure: " + t.getMessage());
            }
        };

        WebSocket webSocket = client.newWebSocket(request, listener);

        // 阻塞等待 WebSocket 关闭
        webSocket.close(1000, "Closed by user");
    }
}
