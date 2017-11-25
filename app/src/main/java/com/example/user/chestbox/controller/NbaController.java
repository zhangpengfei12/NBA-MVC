package com.example.user.chestbox.controller;

import com.example.user.chestbox.bean.NbaMatch;
import com.example.user.chestbox.model.NbaMatchModel;
import com.example.user.chestbox.util.GetDataListener;

import java.util.List;

/**
 * Created by User on 2017/11/24.
 */

public class NbaController {

    private NbaMatchModel mode;

    public NbaController(){ mode = new NbaMatchModel();}

    public void setNbaMatchList(int k,GetDataListener<List> listener){
        mode.setNbaMatch(k, listener);
    }

    public List getlist(){
        return mode.getList();
    }

    public interface onChoiseDateListener {
        void onComplete();
    }
}
