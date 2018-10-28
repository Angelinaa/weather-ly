package com.example.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Adapter;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import android.widget.Adapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import cn.edu.pku.ly.app.MyApplication;
import cn.edu.pku.ly.bean.City;
import com.example.weather.Myadapter;
import cn.edu.pku.ly.db.CityDB;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;
    private ClearEditText mClearEditText;
    private TextView title;
    private ListView mList;
    private Myadapter myadapter;
    private List<City> cityList;
    private List<City> filterDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        initViews();
        mBackBtn=(ImageView)findViewById(R.id.title_back);
        title = findViewById(R.id.title_name);
        mBackBtn.setOnClickListener(this);
    }
    private void initViews(){
        mBackBtn=(ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        mClearEditText=(ClearEditText)findViewById(R.id.search_city);
        mList=(ListView)findViewById(R.id.title_list);
        filterDataList=new ArrayList<City>();
        MyApplication myApplication=(MyApplication)getApplication();
        cityList=myApplication.getCityList();
        for (City city : cityList) {
            filterDataList.add(city);
        }

        myadapter=new Myadapter(filterDataList,this.getLayoutInflater());
        mList.setAdapter(myadapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView,View view,int position,long l){
                City city=filterDataList.get(position);
                Intent i=new Intent();
                i.putExtra("cityCode",city.getNumber());
                title.setText("当前城市："+city.getCity());
                setResult(RESULT_OK,i);
                finish();
            }
        });
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
                mList.setAdapter(myadapter);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void filterData(String filterStr){
        filterDataList=new ArrayList<City>();
        Log.d("Filter",filterStr);
        if(TextUtils.isEmpty(filterStr)){
            for(City city:cityList){
                filterDataList.add(city);
            }
        }else{
            filterDataList.clear();
            for(City city:cityList){
                if(city.getCity().indexOf(filterStr.toString())!=-1){
                    filterDataList.add(city);
                }
            }
        }
        myadapter.updateListView(filterDataList);
    }
    public void onClick(View v){
        switch(v.getId()){
            case R.id.title_back:
                Intent i=new Intent();
                i.putExtra("cityCode","101160101");
                setResult(RESULT_OK,i);
                finish();
                break;
            default:
                break;
        }
    }

}
