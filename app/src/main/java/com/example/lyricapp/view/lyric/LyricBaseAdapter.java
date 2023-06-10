package com.example.lyricapp.view.lyric;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lyricapp.R;

import java.util.ArrayList;
import java.util.List;

public class LyricBaseAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int currentIndex = 0;
    private List<LyricBean> dataList = new ArrayList<>();

    public LyricBaseAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setDataList(List<LyricBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    public List<LyricBean> getDataList() {
        return dataList;
    }

    public void setChooseIndex(int index) {
        currentIndex = index;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_lyric, null);
            viewHolder.tvLyric = convertView.findViewById(R.id.tv_lyric);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvLyric.setText(dataList.get(position).getLyric());
        if (currentIndex == position) {
            viewHolder.tvLyric.setTextColor(Color.RED);
        } else {
            viewHolder.tvLyric.setTextColor(Color.BLACK);
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView tvLyric;
    }
}
