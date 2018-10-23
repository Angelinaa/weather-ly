package com.example.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.*;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Adapter;
import java.util.List;
import android.widget.Adapter;
import cn.edu.pku.ly.app.MyApplication;
import cn.edu.pku.ly.bean.City;

public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;
    private EditText mEditText;
    private ListView mList;
    private Myadapter myadapter;
    private List cityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);
        initViews();
        mBackBtn=(ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }
    private void initViews(){
        mBackBtn=(ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
        mEditText=(EditText)findViewById(R.id.search_city);
        mList=(ListView)findViewById(R.id.title_list);
        MyApplication myApplication=(MyApplication)getApplication();
        cityList=myApplication.getCityList();
        for(City city:cityList){
            filterDataList.add(city);
        }
        myadapter=new Myadapter(SelectCity.this,cityList);
        mList.setAdapter(myadapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> adapterView,View view,int position,long l){
                City city=filterDataList.get(position);
                Intent i=new Intent();
                i.putExtra("cityCode",city.getNumber());
                setResult(RESULT_OK,i);
                finish();
            }
        });
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
