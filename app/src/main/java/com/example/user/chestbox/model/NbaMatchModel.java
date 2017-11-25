package com.example.user.chestbox.model;

import com.example.user.chestbox.bean.NbaMatch;
import com.example.user.chestbox.util.GetDataListener;
import com.example.user.chestbox.util.NbaMatchJsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/11/24.
 */

public class NbaMatchModel {
    private static List list = new ArrayList<>();


    public static void setNbaMatch(int k, final GetDataListener<List> listener) {
        NbaMatchJsonUtils jsonUtils = new NbaMatchJsonUtils();
        try{
            jsonUtils.setNbaMatchlist(k, new NbaMatchJsonUtils.ListListener() {
                @Override
                public void onGetList(List data) {
                    list.addAll(data);
                    if(listener != null){
                        listener.onGetData(data);
                    }
                }
            });
        }catch (Exception e){

        }
    }

    public List getList(){
        return list;
    }
}
