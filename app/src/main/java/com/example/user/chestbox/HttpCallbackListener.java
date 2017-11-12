package com.example.user.chestbox;

/**
 * Created by TK on 2017/11/12.
 */

public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
