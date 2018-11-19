package com.example.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.SelectCity;
import com.example.weather.ViewPagerAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.edu.pku.ly.bean.TodayWeather;
import cn.edu.pku.ly.util.NetUtil;


public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private static final int UPDATE_TODAY_WEATHER=1;
    private ImageView mUpdateBtn;
    private ImageView mCitySelect;
    //private ProgressBar mUpdateProgressBar;
    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv,city_name_Tv;
    private ImageView weatherImg,pmImg;
    //六天天⽓气信息展示
    //显示两个展示⻚页
    private ViewPagerAdapter vpAdapter;
    private ViewPager vp;
    private List<View> views;
    //为引导⻚页增加⼩小圆点
    private ImageView[] dots; //存放⼩小圆点的集合
    private int[] ids = {R.id.iv1,R.id.iv2};
    private TextView
            week_today,temperature,climate,wind,week_today1,temperature1,climate1,wind1
            ,week_today2,temperature2,climate2,wind2;
    private TextView
            week_today3,temperature3,climate3,wind3,week_today4,temperature4,climate4,wind4,week_today5,temperature5,climate5,wind5;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUpdateBtn=(ImageView)findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);

        //mUpdateProgressBar=(ProgressBar)findViewById(R.id.title_update_progress);
        //mUpdateProgressBar.setOnClickListener(this);

        if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
            Log.d("myWeather","网络OK");
            //queryWeatherCode(cityCode);
            Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG).show();
        }
        else{
            Log.d("myWeather","网络挂了");
            Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
        }
        mCitySelect=(ImageView)findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);
        //初始化两个滑动⻚页⾯面
        initViews();
        //⼩小圆点初始化
        initDots();
        //初始化界⾯面控件
        initView();
    }
    void initView(){
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm2_5_num);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.tempe_today);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);

        week_today=views.get(0).findViewById(R.id.week_today);
        temperature=views.get(0).findViewById(R.id.temperature);
        climate=views.get(0).findViewById(R.id.climate);
        wind=views.get(0).findViewById(R.id.wind);

        week_today1=views.get(0).findViewById(R.id.week_today1);
        temperature1=views.get(0).findViewById(R.id.temperature1);
        climate1=views.get(0).findViewById(R.id.climate1);
        wind1=views.get(0).findViewById(R.id.wind1);

        week_today2=views.get(0).findViewById(R.id.week_today2);
        temperature2=views.get(0).findViewById(R.id.temperature2);
        climate2=views.get(0).findViewById(R.id.climate2);
        wind2=views.get(0).findViewById(R.id.wind2);

        week_today3=views.get(1).findViewById(R.id.week_today);
        temperature3=views.get(1).findViewById(R.id.temperature);
        climate3=views.get(1).findViewById(R.id.climate);
        wind3=views.get(1).findViewById(R.id.wind);

        week_today4=views.get(1).findViewById(R.id.week_today1);
        temperature4=views.get(1).findViewById(R.id.temperature1);
        climate4=views.get(1).findViewById(R.id.climate1);
        wind4=views.get(1).findViewById(R.id.wind1);

        week_today5=views.get(1).findViewById(R.id.week_today2);
        temperature5=views.get(1).findViewById(R.id.temperature2);
        climate5=views.get(1).findViewById(R.id.climate2);
        wind5=views.get(1).findViewById(R.id.wind2);


        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
    }
    public void onClick(View view){

        if(view.getId()==R.id.title_city_manager){
            Intent i=new Intent(this,SelectCity.class);
            startActivityForResult(i,1);
        }
        if(view.getId()==R.id.title_update_btn){
            //mUpdateBtn.setVisibility(View.GONE);
            //mUpdateProgressBar.setVisibility(View.VISIBLE);

            SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
            String cityCode=sharedPreferences.getString("main_city_code","101010100");
            Log.d("myWeather",cityCode);
            if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络OK");
                queryWeatherCode(cityCode);
                // Toast.makeText(MainActivity.this, "网络OK", Toast.LENGTH_LONG).show();
            }
            else{
                Log.d("myWeather","网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void queryWeatherCode(String cityCode){

        final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        Log.d("myWeather",address);
        new Thread(new Runnable(){
            public void run(){
                HttpURLConnection con=null;
                TodayWeather todayWeather=null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);
                    con.setReadTimeout(8000);
                    InputStream in = con.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeatherXMLString", responseStr);
                    todayWeather=parseXML(responseStr);
                    if(todayWeather!=null){
                        Log.d("myWeather",todayWeather.toString());

                        Message msg =new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj=todayWeather;
                        mHandler.sendMessage(msg);
                    }
                }catch(Exception e) {
                    e.printStackTrace();
                }finally {
                    if(con!=null){
                        con.disconnect();
                    }
                }
            }
        }).start();

    }
    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather=null;
        int fengxiangCount=0;
        int fengliCount =0;
        int dateCount=0;
        int highCount =0;
        int lowCount=0;
        int typeCount =0;

        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather= new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                                int pm25=Integer.parseInt(todayWeather.getPm25());
                                if(pm25>=0&&pm25<=50){
                                    pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_0_50));
                                } else if(pm25>=51&&pm25<=100){
                                    pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_51_100));
                                }else if(pm25>=101&&pm25<=150){
                                    pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_101_150));
                                }else if(pm25>=151&&pm25<=200){
                                    pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_151_200));
                                } else if(pm25>=201&&pm25<=300){
                                    pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_201_300));
                                } else if(pm25>=301){
                                    pmImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_greater_300));
                                }
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                todayWeather.setWind(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind1(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind2(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind3(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind4(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fengli") && fengliCount == 5) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind5(xmlPullParser.getText());
                                fengliCount++;
                            }else if (xmlPullParser.getName().equals("fl_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWind(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                String h = xmlPullParser.getText().substring(2).trim();
                                todayWeather.setHigh(h);
                                todayWeather.setTemperatureH(h);
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 1) {
                                eventType = xmlPullParser.next();
                                String h = xmlPullParser.getText().substring(2).trim();
                                todayWeather.setTemperatureH1(h);
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 2) {
                                eventType = xmlPullParser.next();
                                String h = xmlPullParser.getText().substring(2).trim();
                                todayWeather.setTemperatureH2(h);
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 3) {
                                eventType = xmlPullParser.next();
                                String h = xmlPullParser.getText().substring(2).trim();
                                todayWeather.setTemperatureH3(h);
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 4) {
                                eventType = xmlPullParser.next();
                                String h = xmlPullParser.getText().substring(2).trim();
                                todayWeather.setTemperatureH4(h);
                                highCount++;
                            }else if (xmlPullParser.getName().equals("high") && highCount == 5) {
                                eventType = xmlPullParser.next();
                                String h = xmlPullParser.getText().substring(2).trim();
                                todayWeather.setTemperatureH5(h);
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                String l = xmlPullParser.getText().substring(2).trim();
                                todayWeather.setLow(l);
                                todayWeather.setTemperatureL(l);
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 1) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL1(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 2) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL2(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 3) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL3(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 4) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL4(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            }else if (xmlPullParser.getName().equals("low") && lowCount == 5) {
                                eventType = xmlPullParser.next();
                                todayWeather.setTemperatureL5(xmlPullParser.getText().substring(2).trim());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                                String type=todayWeather.getType();
                                if(type.equals("晴")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_qing));
                                } else if(type.equals("暴雪")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoxue));
                                }else if(type.equals("暴雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_baoyu));
                                }else if(type.equals("大暴雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dabaoyu));
                                } else if(type.equals("大雪")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_daxue));
                                } else if(type.equals("大雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_dayu));
                                }else if(type.equals("多云")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_duoyun));
                                }else if(type.equals("雷阵雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyu));
                                }else if(type.equals("雷阵雨冰雹")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_leizhenyubingbao));
                                }else if(type.equals("沙尘暴")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_shachenbao));
                                }else if(type.equals("特大暴雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_tedabaoyu));
                                }else if(type.equals("小雪")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoxue));
                                }else if(type.equals("小雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_xiaoyu));
                                }else if(type.equals("阴")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yin));
                                }else if(type.equals("雨夹雪")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_yujiaxue));
                                }else if(type.equals("阵雪")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenxue));
                                }else if(type.equals("阵雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhenyu));
                                }else if(type.equals("中雪")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongxue));
                                }else if(type.equals("中雨")){
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_zhongyu));
                                }else
                                    weatherImg.setImageDrawable(getResources().getDrawable(R.drawable.biz_plugin_weather_wu));

                            }
                        }
                        break;
                    // 判断当前事件是否为标签元素结束事件
                    case XmlPullParser.END_TAG:
                        break;
                }
// 进入下一个元素并触发相应事件
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }
    void updateTodayWeather(TodayWeather todayWeather){
//        if(mUpdateBtn.getVisibility()==View.GONE&&mUpdateProgressBar.getVisibility()==View.VISIBLE){
//            mUpdateProgressBar.setVisibility(View.GONE);
//            mUpdateBtn.setVisibility(View.VISIBLE);
//        }

        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+ "发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力:"+todayWeather.getFengli());
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();

        week_today.setText(todayWeather.getWeek_today());
        temperature.setText(todayWeather.getTemperatureL() + "~"+ todayWeather.getTemperatureH());
        climate.setText(todayWeather.getClimate());
        wind.setText(todayWeather.getWind());

        week_today1.setText(todayWeather.getWeek_today1());
        temperature1.setText(todayWeather.getTemperatureL1() + "~"+ todayWeather.getTemperatureH1());
        climate1.setText(todayWeather.getClimate1());
        wind1.setText(todayWeather.getWind1());

        week_today2.setText(todayWeather.getWeek_today2());
        temperature2.setText(todayWeather.getTemperatureL2() + "~"+ todayWeather.getTemperatureH2());
        climate2.setText(todayWeather.getClimate2());
        wind2.setText(todayWeather.getWind2());

        week_today3.setText(todayWeather.getWeek_today3());
        temperature3.setText(todayWeather.getTemperatureL3() + "~"+ todayWeather.getTemperatureH3());
        climate3.setText(todayWeather.getClimate3());
        wind3.setText(todayWeather.getWind3());

        week_today4.setText(todayWeather.getWeek_today4());
        temperature4.setText(todayWeather.getTemperatureL4() + "~"+ todayWeather.getTemperatureH4());
        climate4.setText(todayWeather.getClimate4());
        wind4.setText(todayWeather.getWind4());

        week_today5.setText(todayWeather.getWeek_today5());
        temperature5.setText(todayWeather.getTemperatureL5() + "~"+ todayWeather.getTemperatureH5());
        climate5.setText(todayWeather.getClimate5());
        wind5.setText(todayWeather.getWind5());


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String newCityCode= data.getStringExtra("cityCode");
            Log.d("myWeather", "选择的城市代码为"+newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(newCityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
            }
        }
    }
    //初始化校⼩小圆点
    void initDots(){
        dots = new ImageView[views.size()];
        for(int i =0;i<views.size();i++){
            dots[i]=(ImageView)findViewById(ids[i]);
        }
    }
    //六天天⽓气信息展示
    private void initViews(){
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.sixday1,null));
        views.add(inflater.inflate(R.layout.sixday2,null));
        vpAdapter = new ViewPagerAdapter(views);
        vp = findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        //为pageviewer配置监听事件
        vp.setOnPageChangeListener(this);
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        for (int a = 0;a<ids.length;a++){
            if(a==position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else {
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }
}

