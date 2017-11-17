package com.example.user.chestbox;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2017/11/16.
 */

public class ZhihuActivity extends Activity {
    private TextView textView;
    private ListView listView;
    private SimpleAdapter adapter;
    private List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zhihu_daily_layout);

        textView = (TextView)findViewById(R.id.zhihu_msg_title);
        listView = (ListView)findViewById(R.id.zhihu_msg_list);

        queryMsglist();

    }

    public void queryMsglist(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = "https://news-at.zhihu.com/api/4/news/latest";
                postDownloadJson(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {

                        try {
                            JSONArray jsonArray = null;
                            JSONObject jsonAll = new JSONObject();
                            jsonAll = new JSONObject(response);
                            jsonArray = new JSONArray(jsonAll.getString("stories"));

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("title", jsonObject.getString("title"));
                                String imageString = jsonObject.getString("images").replace("\\","");

                                map.put("images", getNetPicture(imageString.substring(2,imageString.length()-2)));
                                map.put("id", jsonObject.getString("id"));
                                list.add(map);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showMsg();
                                }
                            });

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

                            }
                        });
                    }
                });
            }
        }).start();

    }

    public void showMsg() {
        adapter = new SimpleAdapter(this, list, R.layout.zhuhu_msg_detail,
                new String[]{"title", "images"},
                new int[]{R.id.msg_title, R.id.msg_image});
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
}
