package com.example.user.chestbox.util;

/**
 * Created by User on 2017/11/23.
 */

public interface CallBackListener {
    void onSuccess(String response);

    void onFailure(String msg);
}
