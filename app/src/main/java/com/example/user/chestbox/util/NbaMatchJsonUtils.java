package com.example.user.chestbox.util;

import com.example.user.chestbox.bean.NbaMatch;
import com.example.user.chestbox.commons.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by User on 2017/11/23.
 */

public class NbaMatchJsonUtils {
    final String[] date = new String[]{"1", "2", "3"};
    List list = new ArrayList();

    public void setNbaMatchlist(final int m, final ListListener listListener){
        GetDownJson getDownJson = new GetDownJson();
        getDownJson.getRequest(Urls.NBA_MATCH, new CallBackListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONArray jsonArray = null;
                    JSONObject jsonAll = new JSONObject();
                    jsonAll = new JSONObject(response);
                    String jsonMatch = jsonAll.getJSONObject("result").getString("list");
                    JSONArray threeday = new JSONArray(jsonMatch);

                    if(m == 0 || m == 2){
                        list.addAll(queryOnedayMatch(threeday, m));
                    }else{
                        list.addAll(queryOnedayMatch(threeday, 1));
                    }
                    if(listListener != null){
                        listListener.onGetList(list);
                    }
                } catch (Exception e) {
                    final String error = e.toString();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String msg) {
                //show(msg)
            }
        });
    }

    public List<NbaMatch> queryOnedayMatch(JSONArray threeday, int k) {

        List list2 = new ArrayList();
        GetNetPicture picture = new GetNetPicture();
        try{
            JSONArray jsonArray = new JSONArray(threeday.getJSONObject(k).getString("tr"));
            for (int i = 0;i<threeday.length();i++){
                JSONObject jsonObject = threeday.getJSONObject(i);
                date[i] = jsonObject.getString("title");
                list2.add(date[i]);
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                NbaMatch nbaMatch = new NbaMatch();

                nbaMatch.setPlayer1(jsonObject.getString("player1"));
                nbaMatch.setPlayer1logobig(picture.getNetPicture(jsonObject.getString("player1logo")));
                nbaMatch.setPlayer2(jsonObject.getString("player2"));
                nbaMatch.setPlayer2logobig(picture.getNetPicture(jsonObject.getString("player2logo")));
                nbaMatch.setScore(jsonObject.getString("score"));

                String status2 = jsonObject.getString("status");
                if (status2 != null) {
                    if (status2 == "1") {
                        status2 = "进行中";
                    } else if (status2 == "2") {
                        status2 = "已结束";
                    } else
                        status2 = "未开始";
                }
                nbaMatch.setStatus(status2);
                list2.add(nbaMatch);
            }
        }catch (Exception e){

        }
        return list2;
    }

    public interface ListListener {
        void onGetList(List<NbaMatch> list);
    }
}
