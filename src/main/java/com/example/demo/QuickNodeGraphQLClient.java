package com.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import java.io.IOException;

public class QuickNodeGraphQLClient {

    public static void main(String[] args) throws IOException {

        //让Java程序走代理，需要在请求前设置代理参数
        String proxyHost = "127.0.0.1";
        String proxyPort = "7890"; // Clash默认的Socks5和http代理端口
        // 对http请求开启代理
        System.setProperty("http.proxyHost", proxyHost);
        System.setProperty("http.proxyPort", proxyPort);
        // 对https也开启代理
        System.setProperty("https.proxyHost", proxyHost);
        System.setProperty("https.proxyPort", proxyPort);

        System.setProperty("https.protocols", "TLSv1.2,TLSv1.1,SSLv3");

        String quickNodeUrl = "https://api.quicknode.com/graphql";
        String apiKey = "";

        OkHttpClient client = new OkHttpClient();

        String query = "  query {\n" +
                "    ethereum {\n" +
                "      collection(contractAddress: \"0x2106c00ac7da0a3430ae667879139e832307aeaa\") {\n" +
                "        name\n" +
                "        symbol\n" +
                "        totalSupply\n" +
                "        holders {\n" +
                "          totalCount\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }";

        JSONObject requestBody = new JSONObject();
        requestBody.put("query", query);

        Request request = new Request.Builder()
                .url(quickNodeUrl)
                .header("Content-Type", "application/json")
                .header("Authorization", apiKey)
                .post(RequestBody.create(MediaType.parse("application/json"), requestBody.toJSONString()))
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response.body().string());
        String responseBody = response.body().string();
        JSONObject jsonResponse = JSONObject.parseObject(responseBody);

        JSONArray transactions = jsonResponse.getJSONObject("data").getJSONArray("transactions");
        System.out.println("Received " + transactions.size() + " transactions:");
        for (int i = 0; i < transactions.size(); i++) {
            JSONObject transaction = transactions.getJSONObject(i);
            System.out.println("Transaction Hash: " + transaction.getString("hash"));
            System.out.println("From: " + transaction.getString("from"));
            System.out.println("To: " + transaction.getString("to"));
            System.out.println("Value: " + transaction.getString("value"));
            System.out.println("-------------------");
        }
    }
}

