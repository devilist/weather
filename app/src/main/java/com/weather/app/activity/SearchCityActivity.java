package com.weather.app.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.weather.app.R;
import com.weather.app.db.WeatherDB;
import com.weather.app.model.CityInformation;
import com.weather.app.util.LogUtil;
import com.weather.app.util.MyApplication;
import com.weather.app.util.Utility;

import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

/**
 * this activity is used to show the search results from all cities
 * <p>Created by zengpu on 15/10/24.
 * @author ZengPu
 */
public class SearchCityActivity extends AppCompatActivity {

    private  final static String TAG = "SearchCityActivity";

    private TextView mTextView;

    /**
     * 没有搜索结果时,显示 『无匹配城市』
     */
    private TextView  noSearchResult;

    /**
     * 用以显示城市搜索结果的ListView
     */
    private ListView cityListView;

    /**
     * cityList的Adapter
     */
    private ArrayAdapter<String> cityListViewAdapter;

    /**
     * 城市搜索结果的集合,通过cityListViewAdapter 在 cityListView 中显示
     */
    private ArrayList<String> searchList = new ArrayList<>();

    /**
     * 城市搜索页面的toolBar. 用toolBar代替ActionBar;toolBar有更好的扩展性
     */
    private Toolbar mToolbar;

    /**
     * toolBar 上用来搜索的SearchView
     */
    private SearchView mSearchView;

    private WeatherDB weatherDB;
    /**
     * 所有城市列表
     */
    private List<CityInformation>  cityInformations;

    private final static  String  MY_KEY= "4f08b54689454ae9bf61905c5a032cb4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city_layout);
        weatherDB = WeatherDB.getInstance();
        initCities();

        mTextView = (TextView) findViewById(R.id.mTextView);
        noSearchResult = (TextView) findViewById(R.id.no_search_result);

        cityListView =(ListView) findViewById(R.id.city_listView);
        cityListView.setVerticalScrollBarEnabled(false);
        cityListViewAdapter = new ArrayAdapter<>(SearchCityActivity.this,
                android.R.layout.simple_list_item_1,searchList);
        cityListView.setAdapter(cityListViewAdapter);

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //获得toolbar 中的searchView 实例,并调用OnQueryTextListener 监听输入内容
        getMenuInflater().inflate(R.menu.searchcityactivity_menu, menu);
        mSearchView = (SearchView) menu.findItem(R.id.city_search).getActionView();
        mSearchView.setIconifiedByDefault(false); // 不显toolbar示图标
        mSearchView.setSubmitButtonEnabled(false);// 显示返回箭头
        mSearchView.setQueryHint("搜索国内城市"); // 设置搜索提示
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText)) {
                    mTextView.setVisibility(View.GONE);
                    noSearchResult.setVisibility(View.GONE);
                    searchList.clear();
                    if (newText.length()>1) {
                        for (int i = 0; i < cityInformations.size(); i++) {
                            String c = cityInformations.get(i).toString();
                            int index = c.indexOf(newText);
                            if (index != -1) {
                                searchList.add(c);
                            }
                        }
                        if (searchList.size()==0) {
                            noSearchResult.setVisibility(View.VISIBLE);
                            cityListView.setVisibility(View.GONE);

                        } else {
                            cityListView.setVisibility(View.VISIBLE);
                            cityListViewAdapter.notifyDataSetChanged();
                            cityListView.setSelection(0);
                        }
                    } else {
                        noSearchResult.setVisibility(View.VISIBLE);
                        cityListView.setVisibility(View.GONE);
                    }
                } else {
                    mTextView.setVisibility(View.VISIBLE);
                    cityListView.setVisibility(View.GONE);
                    noSearchResult.setVisibility(View.GONE);
                    searchList.clear();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 从数据库加载全国城市信息
     */
    private void initCities() {
        cityInformations = weatherDB.loadCityInformation();
        if (cityInformations.size() <= 0) {
            queryFromServer();
        }
    }

    /**
     * 从服务器请求全国城市数据
     */
    private void queryFromServer() {
        String address= "http://api.36wu.com/Weather/GetAreaList?&format=json&authkey="+MY_KEY;
        RequestQueue mQueue = Volley.newRequestQueue(MyApplication.getContext());
        StringRequest stringRequest = new StringRequest(address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        LogUtil.i(TAG, "--------> " + response);
                        boolean result = false;
                        result = Utility.handleCityInfoResponse(weatherDB,response);
                        if (result) {
                            // 通过runonUiMainThread方法返回主线程处理逻辑
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    closeProgressDialog();
                                    initCities();
                                }
                            });
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                LogUtil.d(TAG, "-------------------->" + volleyError);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        closeProgressDialog();
                        Toast.makeText(MyApplication.getContext(),
                                "服务器未响应,加载数据失败！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }) {
            //重写Velloy框架 StringRequest.parseNetworkResponse 方法,将解析字符方式改为 UTF-8;
            protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";
            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                try {
                    String type = response.headers.get(HTTP.CONTENT_TYPE);
                    if (type == null) {
                        type = TYPE_UTF8_CHARSET;
                        response.headers.put(HTTP.CONTENT_TYPE, type);
                    } else if (!type.contains("UTF-8")) {
                        type += ";" + TYPE_UTF8_CHARSET;
                        response.headers.put(HTTP.CONTENT_TYPE, type);
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, "-------------------->" + e);
                    }
                return super.parseNetworkResponse(response);
            }
        } ;
        mQueue.add(stringRequest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherDB.destroyDB();
    }
}
