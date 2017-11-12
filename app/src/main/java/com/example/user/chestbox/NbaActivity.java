package com.example.user.chestbox;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by User on 2017/11/10.
 */

public class NbaActivity extends Activity {
    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String,Object>> matchList;
    private TextView publishText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nba_match_layout);

        queryMatchList();
        publishText = (TextView)findViewById(R.id.error_msg);



    }

    public void showMatch(){
        adapter = new SimpleAdapter(this,matchList, R.layout.match_detail,
                new String[]{"player1","player1logobig","score","player2logobig","player2"},
                new int[]{R.id.team1_name,R.id.team1_logo,R.id.match_point,R.id.team2_logo,R.id.team2_name});
        listView = (ListView)findViewById(R.id.match_list);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                //判断是否为我们要处理的对象
                if(view instanceof ImageView && data instanceof Bitmap){
                    ImageView iv = (ImageView) view;

                    iv.setImageBitmap((Bitmap) data);
                    return true;
                }else
                    return false;
            }
        });
        listView.setAdapter(adapter);
        publishText.setVisibility(View.GONE);
    }

    public Bitmap getNetPicture(String url){

        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public void queryMatchList(){
        new Thread(new Runnable(){
            @Override
            public void run() {
                // TODO
                // 在这里进行 http request.网络请求相关操作
                String address = "http://op.juhe.cn/onebox/basketball/nba?key=2e0711aa844c4347e4f0efaf411ba93a";
                postDownloadJson(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();

                        try {
                            JSONArray jsonArray = null;
                            JSONObject jsonAll = new  JSONObject();
                            jsonAll = new JSONObject(response);
                            String jsonMatch = jsonAll.getJSONObject("result").getString("list");
                            String jsonMatch2 = jsonMatch.substring(1,jsonMatch.length()-1);
                            String jsonMatch3 = new JSONObject(jsonMatch2).getString("tr");
                            String jsonMatch4 = jsonMatch3.substring(1,jsonMatch3.length()-1);
                            jsonArray = new JSONArray(jsonMatch3);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                // 初始化map数组对象
                                Map<String, Object> map = new HashMap<String, Object>();
                                //map.put("title", jsonObject.getString("title"));
                                map.put("player1", jsonObject.getString("player1"));
                                map.put("player2", jsonObject.getString("player2"));
                                map.put("player1logobig", getNetPicture(jsonObject.getString("player1logo")));
                                map.put("player2logobig", getNetPicture(jsonObject.getString("player2logo")));
                                map.put("score", jsonObject.getString("score"));
                                list.add(map);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        matchList = list;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                showMatch();
                            }
                        });
                    }

                    @Override
                    public void onError(Exception e) {
                        final String error = e.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                publishText.setText(error);
                            }
                        });
                    }
                });
            }
        }).start();

    }

    public static void postDownloadJson(final String address,final HttpCallbackListener listener) {
        new Thread(new Runnable(){
            @Override
            public void run(){
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
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
                    if(listener != null){
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                    if(listener != null){
                        listener.onError(e);
                    }
                }finally {
                    if(connection != null){
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }
}

