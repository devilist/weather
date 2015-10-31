package com.weather.app.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weather.app.R;
import com.weather.app.db.WeatherDB;
import com.weather.app.model.CityInformation;
import com.weather.app.util.ActivityCollector;
import com.weather.app.util.InitWeatherInfo;
import com.weather.app.util.LogUtil;
import com.weather.app.util.MyApplication;
import com.weather.app.util.Utility;
import com.weather.app.view.DailyForecastChartView;
import com.weather.app.view.ForecastAdapter;
import com.weather.app.view.HourlyChartView;
import com.weather.app.view.HourlyForecastAdapter;
import com.weather.app.view.HourlyForecastChartView;
import com.weather.app.view.MyGridView;
import com.weather.app.view.MyViewPagerAdapter;
import com.weather.app.view.SuggestionAdapter;
import com.weather.app.view.TodayWeatherInfoAdapter;
import com.weather.app.view.ViewPagerDepthPageTransformer;

import org.achartengine.GraphicalView;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by zengpu on 15/10/31.
 */
public class SelectedCityWeatherActivity extends BaseActivity {

    /**
     * 天气显示界面的toolbar
     */
    private Toolbar mToolbar;

    /**
     * 显示天气的界面集合
     */
    private List<View> viewList = new ArrayList<>();

    /**
     * 左右滑动显示不同城市的天气
     */
    private ViewPager mViewPager;

    /**
     * 所有在ViewPager中显示天气的城市集合
     */
    private List<CityInformation> mCities;

    private WeatherDB weatherDB;

    private TextView showCityName;

    /**
     * 天气界面主布局
     */
//    private LinearLayout weather_layout;


    private int keyFlag = 0;
    private int firstStartFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selected_city_weather_layout);
        weatherDB = WeatherDB.getInstance();
        showCityName = (TextView) findViewById(R.id.show_city_name);

//        weather_layout = (LinearLayout)findViewById(R.id.weather_layout);
        //初始化 Toolbar
        mToolbar = (Toolbar) findViewById(R.id.city_weather_Toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationIcon(R.mipmap.ic_plus3);
        setOverflowShowingAlways();

        //初始化ViewPager
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        initMyCities();
        mViewPager.setAdapter(new MyViewPagerAdapter(viewList));
        initCurrentItem();
        mViewPager.setVerticalScrollBarEnabled(false);
        mViewPager.setPageTransformer(true, new ViewPagerDepthPageTransformer());
//        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                CityInformation cityInformation = mCities.get(position);
                String cityName = cityInformation.getDistrict();
                showCityName.setText(cityName);

                //控制toolbar的行为
                View view = viewList.get(position);
//
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (state == 1) {
//                    showCityName.setVisibility(View.GONE);
//                }
//                if (state == 2 | state == 0 ) {
//                    showCityName.setVisibility(View.VISIBLE);
//                }
            }
        });

//        //获取该程序的安装包路径
//        String path=MyApplication.getContext().getPackageResourcePath();
//        //获取程序默认数据库路径  /data/data/com.weather.app/databases/weather
//        String databasePath = getApplicationContext().getDatabasePath("weather").getAbsolutePath();
//        //获取当前程序路径
//        String thispath = getApplicationContext().getFilesDir().getAbsolutePath();
//        LogUtil.i("TAG", "安装路径是 : " + path);
//        LogUtil.i("TAG", "数据库路径是 : " + databasePath);
//        LogUtil.i("TAG", "当前程序路径是 : " + thispath);

    }

    /**
     * 从其他Activity打开SelectedCityWeatherActivity
     * @param context
     */
    public static void actionStart(Context context , int position) {
        Intent intent = new Intent(context,SelectedCityWeatherActivity.class);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    /**
     * 将searchCityWatherActivity设置成了singleInstance或singleTask模式,
     * <p>该模式下Activity无法接收intent传入的data,需要重写onNewIntent方法
     * <p>在onNewIntent方法中初始化viewPager的currentItem
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

    }

    /**
     * 初始化需要显天气的城市.通过数据库加载所有被标记为ture的城市,存储在mCities中;
     * <p>如果mCities为空,则转到SearchCityActivity界面,若mCities不为空,则为每一个
     * <p>city创建view用以显示天气
     */
    private void initMyCities(){

        mCities = weatherDB.loadMyCities();
        LogUtil.i("TAG", "mcities size is " + mCities.size());

        if (mCities.size()==0){
            SearchCityActivity.actionStart(SelectedCityWeatherActivity.this, null);
        } else {
            for (int i = 0; i<mCities.size() ;i++) {

                String cityCode = String.valueOf(mCities.get(i).getAreaid());
                View weatherInformation = getLayoutInflater().from(this)
                        .inflate(R.layout.weather_info_layout, null);
                queryWeatherInfo(cityCode,weatherInformation);
                viewList.add(weatherInformation);
                LogUtil.i("TAG", "viewlist size is " + viewList.size());
            }
        }
    }

    /**
     * 初始化ViewPager的currentItem
     * 首次启动,currentItem = 0;
     * 新添加城市,currentItem = mCities.get(mCities.size()-1);
     * 更换城市,currentItem = mCities.get(position);
     */
    private void initCurrentItem(){

        CityInformation cityInformation;
        String cityName;
        Intent intent= getIntent();
        int position = intent.getIntExtra("position",-2);

        LogUtil.i("TAG", "position is " + position);

        if (mCities.size()==0){
            SearchCityActivity.actionStart(SelectedCityWeatherActivity.this, null);
        } else if (position == -2) {
            cityInformation = mCities.get(0);
            cityName = cityInformation.getDistrict();
            // 初始化toolbar 中的TextView
            showCityName.setText(cityName);
            //false,关闭切换动画
            mViewPager.setCurrentItem(0,false);
            LogUtil.i("TAG", "first city name is " + cityName);
        } else if (position == -1) {
            cityInformation = mCities.get(mCities.size()-1);
            cityName = cityInformation.getDistrict();
            // 初始化toolbar 中的TextView
            showCityName.setText(cityName);
            mViewPager.setCurrentItem(mCities.size() - 1,false);
            LogUtil.i("TAG", "last city name is " + cityName);
        } else {
            cityInformation = mCities.get(position);
            cityName = cityInformation.getDistrict();
            showCityName.setText(cityName);
            mViewPager.setCurrentItem(position,false);
            LogUtil.i("TAG", "current city name is " + cityName);
        }
    }

    /**
     * 查询天气信息
     * @param cityCode   城市代码
     * @param v  显示天气的界面
     */
    private void queryWeatherInfo(String cityCode , View v ) {
//        https://api.heweather.com/x3/weather?cityid=CN101210403&key=3662fc06590e42f0a509fdcc536a8c5f
//        Key1 8ea01e64ec0c4fbd8330e863ec0712da
//        Key2 3662fc06590e42f0a509fdcc536a8c5f
//        key3 3b712d77b7554facb0e4b7e2d9fb6bd6

        String address;
        address = "https://api.heweather.com/x3/weather?cityid=CN" +
                cityCode +"&key=3b712d77b7554facb0e4b7e2d9fb6bd6";
        if (keyFlag > 0){
             switch (keyFlag) {
                 case 1:
                     address = "https://api.heweather.com/x3/weather?cityid=CN" +
                             cityCode +"&key=8ea01e64ec0c4fbd8330e863ec0712da";
                     break;
                 default:
                     address = "https://api.heweather.com/x3/weather?cityid=CN" +
                             cityCode +"&key=3662fc06590e42f0a509fdcc536a8c5f";
             }
        }
        queryFromServer(address, v);
    }

    /**
     * 使用volley从服务器查询天气信息
     * @param address
     * @param v
     */
    private void queryFromServer(final String address, final View v){
        RequestQueue mQueue = Volley.newRequestQueue(MyApplication.getContext());
        StringRequest stringRequest = new StringRequest(address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utility.handleWeatherResponse(MyApplication.getContext(),response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showWeather(v);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(MyApplication.getContext(),"同步失败!", Toast.LENGTH_SHORT).show();
                        keyFlag++;
            }
        });
        mQueue.add(stringRequest);
    }

    /**
     * 显示天气
     * @param v 显示天气的界面
     */
    private void showWeather(View v) {

        final InitWeatherInfo initWeatherInfo = new InitWeatherInfo(MyApplication.getContext());
        final Typeface tf = Typeface.createFromAsset(MyApplication.getContext().getAssets(),
                "fonts/NotoSansCJKsc-Thin.otf");

        //加载控件
        TextView temp = (TextView) v.findViewById(R.id.temp);
        TextView tempSymbol = (TextView) v.findViewById(R.id.temp_symbol);
        ImageView imageView = (ImageView) v.findViewById(R.id.my_image);
        TextView condition = (TextView) v.findViewById(R.id.condition);
        TextView cityView = (TextView) v.findViewById(R.id.city_name);
        TextView currentTime = (TextView) v.findViewById(R.id.current_time);
        ImageView position = (ImageView) v.findViewById(R.id.position);
        TextView updateTime = (TextView) v.findViewById(R.id.update_time);
        TextView mDay = (TextView) v.findViewById(R.id.week_day);

        final RelativeLayout hourlyForecastChatView = (RelativeLayout) v.findViewById(
                R.id.hourlyforecast_chart_view_layout);
        final RelativeLayout dailyForecastChartView = (RelativeLayout) v.findViewById(
                R.id.dailyforecast_chart_view_layout);

        final GridView hours = (GridView) v.findViewById(R.id.hourly_forecast_gridview);
        final GridView daily = (GridView) v.findViewById(R.id.forecast_gridview);

        MyGridView todayWeatherInfo = (MyGridView) v.findViewById(R.id.hum_vis_gridview);
        MyGridView suggestion = (MyGridView) v.findViewById(R.id.suggestion_gridview);

        temp.setText(initWeatherInfo.getBaseWeatherInfo().get(0));
        tempSymbol.setText(initWeatherInfo.getBaseWeatherInfo().get(1));
//        String condCode = prefs.getString("cond_code","");
        Utility.setCondImage(initWeatherInfo.getBaseWeatherInfo().get(2), imageView);
//        devider1.setVisibility(View.VISIBLE);
        condition.setText(initWeatherInfo.getBaseWeatherInfo().get(3));
        cityView.setText(initWeatherInfo.getBaseWeatherInfo().get(4));
        position.setVisibility(View.VISIBLE);
        updateTime.setText(initWeatherInfo.getBaseWeatherInfo().get(5));
        mDay.setText(initWeatherInfo.getBaseWeatherInfo().get(6));


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date curDate = new Date(System.currentTimeMillis());
        String curDateStr = sdf.format(curDate);
        currentTime.setText(curDateStr);
//        LogUtil.i("date", "date is " + curDateStr);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //chartView 数据准备
                HourlyForecastChartView hfc = new HourlyForecastChartView();
                GraphicalView graphicalView1 = hfc.getChartGraphicalView(
                        MyApplication.getContext(), tf, initWeatherInfo.getTitles1(),
                        initWeatherInfo.getMySrcHours(),
                        initWeatherInfo.getxValues_hourlyForecast(),
                        initWeatherInfo.getyValues_hourlyForecast());
//                hourlyForecastChatView.addView(graphicalView1);

                HourlyChartView hcv = new HourlyChartView(MyApplication.getContext());

                ColumnChartView lcv = hcv.getLineChartView(MyApplication.getContext(),tf,
                                        initWeatherInfo.getHourlyForecastHours(),
                                        initWeatherInfo.getxValues_hourlyForecast(),
                                        initWeatherInfo.getyValues_hourlyForecast()
                        );
                hourlyForecastChatView.addView(lcv);

                HourlyForecastAdapter hourlyForecastAdapter = new HourlyForecastAdapter(
                        MyApplication.getContext(),
                        initWeatherInfo.getHourlyForecastHours());
                hours.setAdapter(hourlyForecastAdapter);

                DailyForecastChartView dfc = new DailyForecastChartView();
                GraphicalView graphicalView = dfc.getChartGraphicalView(
                        MyApplication.getContext(), tf, initWeatherInfo.getTitles(),
                        initWeatherInfo.getMySrcDates(),
                        initWeatherInfo.getxValues_dailyForecast(),
                        initWeatherInfo.getyValues_dailyForecast());
                dailyForecastChartView.addView(graphicalView);

                //forecast
                ForecastAdapter forecastAdapter = new ForecastAdapter(MyApplication.getContext(),
                        initWeatherInfo.getDailyForecastWeatherInfo().get(0),
                        initWeatherInfo.getDailyForecastWeatherInfo().get(1));
                daily.setAdapter(forecastAdapter);

            }
        });

        //todayWeatherinfo
        TodayWeatherInfoAdapter todayWeatherInfoAdapter = new TodayWeatherInfoAdapter(
                MyApplication.getContext(), initWeatherInfo.getOtherWeatherInfo().get(0),
                initWeatherInfo.getOtherWeatherInfo().get(1));
        todayWeatherInfo.setAdapter(todayWeatherInfoAdapter);

        SuggestionAdapter suggestionAdapter = new SuggestionAdapter(MyApplication.getContext(),
                                                initWeatherInfo.getSuggestion().get(0),
                                                initWeatherInfo.getSuggestion().get(1),
                                                initWeatherInfo.getSuggestionitemImag());

        suggestion.setAdapter(suggestionAdapter);

        //gridview的点击事件
        suggestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String title = initWeatherInfo.getSuggestion().get(0).get(position);
                String message = initWeatherInfo.getSuggestion().get(2).get(position);

                AlertDialog.Builder dialog = new AlertDialog.Builder(SelectedCityWeatherActivity.
                        this, R.style.MyDialog);
                dialog.setTitle(title);
                dialog.setMessage(message);
                dialog.setCancelable(true);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.selectedcity_weather_activity_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                int position = mViewPager.getCurrentItem();
                String currentCity = mCities.get(position).getDistrict();
                CityManagementActivity.actionStart(SelectedCityWeatherActivity.this,currentCity);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ActivityCollector.finishAll();
    }

    private void setOverflowShowingAlways() {
            try {
                    ViewConfiguration config = ViewConfiguration.get(this);
                    Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
                    menuKeyField.setAccessible(true);
                    menuKeyField.setBoolean(config, false);
                } catch (Exception e) {
                     e.printStackTrace();
                }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherDB.destroyDB();
    }
}
