package com.example.user.chestbox.adaptor;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.user.chestbox.R;
import com.example.user.chestbox.bean.NbaMatch;

import java.util.List;

/**
 * Created by User on 2017/11/24.
 */

public class NbaAdaptor extends BaseAdapter {
    private List<NbaMatch> list;
    private LayoutInflater mInflater;

    public NbaAdaptor(Context context, List<NbaMatch> list){
        this.list = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.match_detail, parent,false);
        }
        final ViewHolder holder = getViewHolder(convertView);
        NbaMatch nbaMatch = list.get(position);
        holder.player1.setText(nbaMatch.getPlayer1());
        holder.player2.setText(nbaMatch.getPlayer2());
        holder.player1logobig.setImageBitmap(nbaMatch.getPlayer1logobig());
        holder.player2logobig.setImageBitmap(nbaMatch.getPlayer2logobig());
        holder.score.setText(nbaMatch.getScore());
        holder.status.setText(nbaMatch.getStatus());
        return convertView;
    }

    private ViewHolder getViewHolder(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        if (holder == null) {
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        return holder;
    }

    private class ViewHolder {

        private TextView player1;
        private ImageView player1logobig;
        private TextView score;
        private ImageView player2logobig;
        private TextView player2;
        private TextView status;

        ViewHolder(View view) {
            player1 = (TextView) view.findViewById(R.id.team1_name);
            player2 = (TextView) view.findViewById(R.id.team2_name);
            player1logobig = (ImageView)view.findViewById(R.id.team1_logo);
            player2logobig = (ImageView)view.findViewById(R.id.team2_logo);
            score = (TextView) view.findViewById(R.id.match_point);
            status = (TextView) view.findViewById(R.id.status);
        }
    }
}
