package com.example.user.chestbox;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends Activity {

    private ListView listView;

    private SimpleAdapter adapter;

    private List<Map<String,Object>> mainList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        mainList = initMainList();
        adapter = new SimpleAdapter(this,mainList, R.layout.main_list_layout,
                new String[]{"logo","title","introduction"},new int[]{R.id.list_logo,R.id.list_title,R.id.list_introduction});
        listView = (ListView)findViewById(R.id.main_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent, View view, int position, long id){

                switch (position){
                    case 0:
                        Intent intent = new Intent(MainActivity.this,NbaActivity.class);
                        startActivity(intent);
                        Toast.makeText(MainActivity.this,"NBA",Toast.LENGTH_SHORT).show();
                        break;

                    case 1:
                        Toast.makeText(MainActivity.this,"尚未实现，没有接口",Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(MainActivity.this,"尚未实现，没有接口2",Toast.LENGTH_SHORT).show();
                        break;

                    case 3:
                        Toast.makeText(MainActivity.this,"我也不知道点到了什么",Toast.LENGTH_SHORT).show();
                        break;
                    default:{
                        Toast.makeText(MainActivity.this,"我也不知道你点到了什么",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

            }
        });
    }

    private List<Map<String,Object>> initMainList(){
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>(2);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title","NBA");
        map.put("introduction","新鲜资讯 一手掌握 无毒无害 不用会员");
        map.put("logo",R.drawable.nba_logo);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title","知乎");
        map.put("introduction","与世界分享你的知识、经验和见解");
        map.put("logo",R.drawable.zhihu_logo);
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title","新浪微博");
        map.put("introduction","娱乐休闲生活服务的信息分享和交流平台");
        map.put("logo",R.drawable.xinlang_logo);
        list.add(map);

        return list;
    }
}
