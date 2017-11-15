package com.example.user.chestbox;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import java.lang.reflect.Array;
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
    private List<Map<String, Object>> matchList;
    private TextView publishText;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    private Button today;
    private Button yesterday;
    private Button tomorrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nba_match_layout);

        queryMatchList(1);
        publishText = (TextView) findViewById(R.id.error_msg);

        today = (Button)findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                queryMatchList(1);
            }
        });

        yesterday = (Button)findViewById(R.id.yesterday);
        yesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryMatchList(0);
            }
        });

        tomorrow = (Button)findViewById(R.id.tomorrow);
        tomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryMatchList(2);
            }
        });




    }

    public void showMatch() {
        adapter = new SimpleAdapter(this, matchList, R.layout.match_detail,
                new String[]{"player1", "player1logobig", "score", "player2logobig", "player2", "status"},
                new int[]{R.id.team1_name, R.id.team1_logo, R.id.match_point, R.id.team2_logo, R.id.team2_name, R.id.status});
        listView = (ListView) findViewById(R.id.match_list);
        adapter.setViewBinder(new SimpleAdapter.ViewBinder() {

            public boolean setViewValue(View view, Object data,
                                        String textRepresentation) {
                //判断是否为网络图片
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView iv = (ImageView) view;

                    iv.setImageBitmap((Bitmap) data);
                    return true;
                } else
                    return false;
            }
        });
        listView.setAdapter(adapter);
        publishText.setVisibility(View.GONE);
    }

    public Bitmap getNetPicture(String url) {

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

    public void queryMatchList(final int m) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO
                // 在这里进行 http request.网络请求相关操作
                String address = "http://op.juhe.cn/onebox/basketball/nba?key=2e0711aa844c4347e4f0efaf411ba93a";
                postDownloadJson(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {

                        try {
                            JSONArray jsonArray = null;
                            JSONObject jsonAll = new JSONObject();
                            jsonAll = new JSONObject(response);
                            String jsonMatch = jsonAll.getJSONObject("result").getString("list");
                            JSONArray threeday = new JSONArray(jsonMatch);

                            if(m == 0 || m == 2){
                                queryOnedayMatch(threeday, m);
                            }else{
                                queryOnedayMatch(threeday, 1);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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

    public void queryOnedayMatch(JSONArray threeday, int k) {
        final String[] date = new String[]{"1","2","3"};
        try {
            list.clear();
            JSONArray jsonArray = new JSONArray(threeday.getJSONObject(k).getString("tr"));

            for (int i = 0;i<threeday.length();i++){
                JSONObject jsonObject = threeday.getJSONObject(i);
                date[i] = jsonObject.getString("title");
            }
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
                String status2 = jsonObject.getString("status");
                if (status2 != null) {
                    if (status2 == "1") {
                        status2 = "进行中";
                    } else if (status2 == "2") {
                        status2 = "已结束";
                    } else
                        status2 = "未开始";
                }
                map.put("status", status2);
                list.add(map);
            }
            matchList = list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                yesterday.setText(date[0]);
                today.setText(date[1]);
                tomorrow.setText(date[2]);
                showMatch();
            }
        });

    }

    public static void postDownloadJson(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
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
                    if (listener != null) {
                        listener.onFinish(response.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
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

