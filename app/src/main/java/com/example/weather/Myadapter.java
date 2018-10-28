package com.example.weather;

import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import cn.edu.pku.ly.bean.City;

public class Myadapter extends BaseAdapter{
    private List<City> mList;
    private LayoutInflater mInflater;
    public Myadapter(List<City> lcity, LayoutInflater layoutInflater){
        mList=lcity;
        mInflater=layoutInflater;
    }
    @Override
    public int getCount() {
        return mList.size();
    }
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //获得ListView中的view
        View v = mInflater.inflate(R.layout.item,null);
        //获得对象
        City c = mList.get(position);
        //获得自定义布局中每一个控件的对象。
        TextView i = (TextView) v.findViewById(R.id.item_textview);
        //将数据一一添加到自定义的布局中。
        i.setText(c.getCity()+c.getNumber());

        return v ;
    }
    public void updateListView(List<City> mList){
        this.mList=mList;

    }
}
