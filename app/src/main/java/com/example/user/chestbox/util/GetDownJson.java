package com.example.user.chestbox.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 2017/11/24.
 */

public class GetDownJson {


    public GetDownJson(){
    }

    public static void getRequest(final String url,final CallBackListener callBackListener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL address = new URL(url);
                    connection = (HttpURLConnection) address.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    if (callBackListener != null) {
                        callBackListener.onSuccess(response.toString());
                    }
                } catch (Exception e) {
                    if (callBackListener != null) {
                        callBackListener.onFailure("请求失败");
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
