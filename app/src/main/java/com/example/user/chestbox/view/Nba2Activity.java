package com.example.user.chestbox.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.user.chestbox.HttpCallbackListener;
import com.example.user.chestbox.R;
import com.example.user.chestbox.adaptor.NbaAdaptor;
import com.example.user.chestbox.bean.NbaMatch;
import com.example.user.chestbox.controller.NbaController;
import com.example.user.chestbox.model.NbaMatchModel;
import com.example.user.chestbox.util.CallBackListener;
import com.example.user.chestbox.util.GetDataListener;

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
 * Created by User on 2017/11/10.
 */

public class Nba2Activity extends Activity implements View.OnClickListener{

    private NbaController nbaController;

    private ListView listView;
    private NbaAdaptor adapter;
    private List<NbaMatch> matchList;
    private TextView publishText;
    private Button today;
    private Button yesterday;
    private Button tomorrow;
    private ProgressDialog progressDialog;
    private static int requestCount = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.nba_match_layout);

        today = (Button)findViewById(R.id.today);
        yesterday = (Button)findViewById(R.id.yesterday);
        tomorrow = (Button)findViewById(R.id.tomorrow);
        publishText = (TextView)findViewById(R.id.error_msg);

        today.setOnClickListener(this);
        yesterday.setOnClickListener(this);
        tomorrow.setOnClickListener(this);

        nbaController = new NbaController();
        today.performClick();
        matchList = nbaController.getlist();
        adapter = new NbaAdaptor(this,matchList);
        listView = (ListView)findViewById(R.id.match_list);
        listView.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.today:
                today.setTextColor(Color.BLACK);
                tomorrow.setTextColor(Color.GRAY);
                yesterday.setTextColor(Color.GRAY);
                showProgressDialog();
                nbaController.setNbaMatchList(1, new GetDataListener<List>() {
                    @Override
                    public void onGetData(final List nbaMatches) {
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                if(requestCount == 1){
                                    yesterday.setText((String)nbaMatches.get(0));
                                    today.setText((String)nbaMatches.get(1));
                                    tomorrow.setText((String)nbaMatches.get(2));

                                    requestCount += 1;
                                }
                                matchList.clear();
                                matchList.addAll((List<NbaMatch>)nbaMatches.subList(3,nbaMatches.size()));
                                adapter.notifyDataSetChanged();
                                closeProgressDialog();
                            }
                        });
                    }
                });
                break;

            case R.id.yesterday:
                yesterday.setTextColor(Color.BLACK);
                tomorrow.setTextColor(Color.GRAY);
                today.setTextColor(Color.GRAY);
                showProgressDialog();
                nbaController.setNbaMatchList(0, new GetDataListener<List>() {
                    @Override
                    public void onGetData(final List nbaMatches) {
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                matchList.clear();
                                matchList.addAll((List<NbaMatch>)nbaMatches.subList(3,nbaMatches.size()));
                                adapter.notifyDataSetChanged();
                                closeProgressDialog();
                            }
                        });

                    }
                });
                break;

            case R.id.tomorrow:
                tomorrow.setTextColor(Color.BLACK);
                today.setTextColor(Color.GRAY);
                yesterday.setTextColor(Color.GRAY);
                showProgressDialog();
                nbaController.setNbaMatchList(2, new GetDataListener<List>() {
                    @Override
                    public void onGetData(final List nbaMatches) {
                        listView.post(new Runnable() {
                            @Override
                            public void run() {
                                matchList.clear();
                                matchList.addAll((List<NbaMatch>)nbaMatches.subList(3,nbaMatches.size()));
                                adapter.notifyDataSetChanged();
                                closeProgressDialog();
                            }
                        });

                    }
                });
                break;

        }
    }




    //显示进度对话框
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    //关闭对话框
    private  void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}

