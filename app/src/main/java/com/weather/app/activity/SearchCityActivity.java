package com.weather.app.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.weather.app.R;
import com.weather.app.db.WeatherDB;
import com.weather.app.model.AllCities;
import com.weather.app.model.CityInformation;
import com.weather.app.model.JsonResponse;
import com.weather.app.model.MyCityInformations;
import com.weather.app.util.LogUtil;
import com.weather.app.util.MyApplication;
import com.weather.app.view.QuickSearchViewAdapter;

import org.apache.http.protocol.HTTP;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * this activity is used to show the search results from all cities
 * <p>Created by zengpu on 15/10/24.
 * @author ZengPu
 */
public class SearchCityActivity extends BaseActivity {

    private  final static String TAG = "SearchCityActivity";

    /**
     * 城市搜索页面的toolBar. 用toolBar代替ActionBar;toolBar有更好的扩展性
     */
    private Toolbar mToolbar;

    /**
     * toolBar 上用来搜索的SearchView
     */
    private SearchView mSearchView;

    /**
     * 快速搜索界面
     */
    private GridView quickSearchGridView;

    /**
     * 快速搜索界面的Adapter
     */
    private ArrayAdapter<String> quickSearchViewAdapter;

    private  String[] quickSearchList;

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
     * 没有搜索结果时,显示 『无匹配城市』
     */
    private TextView  noSearchResult;

    private TextView quickAdd;

    private WeatherDB weatherDB;
    /**
     * 所有城市列表
     */
    private List<CityInformation>  cityInformations = new ArrayList<>();

    /**
     * 我的城市
     */
    private List<String> mCitiesList = new ArrayList<>();


    private JsonResponse j;

    private MyCityInformations myCityInformations;

    private final static  String  MY_KEY= "4f08b54689454ae9bf61905c5a032cb4";

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_city_layout);
        weatherDB = WeatherDB.getInstance();
        j= JsonResponse.getInstance();

        new InitCityInformationsTask().execute();

        LogUtil.d(TAG, "leng is " + j.getS1().length());

        mToolbar = (Toolbar) findViewById(R.id.mToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        quickAdd = (TextView) findViewById(R.id.quick_add);
        noSearchResult = (TextView) findViewById(R.id.no_search_result);

        quickSearchGridView = (GridView) findViewById(R.id.quick_search_gridView);

        Intent intent = getIntent();
        mCitiesList = intent.getStringArrayListExtra("mcities");
        quickSearchList =new String[]{
                "定位","北京","上海","广州","深圳","珠海","佛山","南京","苏州",
                "杭州","济南","青岛","郑州","石家庄","福州","厦门","武汉","长沙",
                "成都","重庆","太原","沈阳","南宁","西安"
        };
//        quickSearchViewAdapter =new ArrayAdapter<String>(SearchCityActivity.this,
//                R.layout.quick_search_item,quickSearchList);
        quickSearchViewAdapter = new QuickSearchViewAdapter(SearchCityActivity.this,
                R.layout.quick_search_item,quickSearchList, mCitiesList);
        quickSearchGridView.setVerticalScrollBarEnabled(false);
        quickSearchGridView.setAdapter(quickSearchViewAdapter);
        quickSearchGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView cityName = (TextView) view.findViewById(R.id.item_city_name);
                List<CityInformation> findCity = DataSupport.where("district = ?",
                                                                cityName.getText().toString())
                                                                .find(CityInformation.class);
                //如果该城市已被选择,不触发点击
                if (!findCity.get(0).isSelectedTag()) {
                    mSearchView.setQuery((cityName).getText().toString(), false);
                }
            }
        });

        cityListView =(ListView) findViewById(R.id.city_listView);
        cityListView.setVerticalScrollBarEnabled(false);
        cityListViewAdapter = new ArrayAdapter<>(SearchCityActivity.this,
                R.layout.search_result_item,searchList);
        cityListView.setAdapter(cityListViewAdapter);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] s = searchList.get(position).split(" ");
                String cityName = s[0];
                LogUtil.i("TAG","cityName is"+ cityName +";");
                List<CityInformation> city = DataSupport.where("district = ?",cityName)
                                                        .find(CityInformation.class);
                weatherDB.markSelectedCity(city.get(0));
                SelectedCityWeatherActivity.actionStart(SearchCityActivity.this , -1);
                finish();
            }
        });


    }

    public static void actionStart(Context context,ArrayList<String> mcityList){
        Intent intent = new Intent(context,SearchCityActivity.class);
        intent.putStringArrayListExtra("mcities", mcityList);
        context.startActivity(intent);
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (!TextUtils.isEmpty(newText)) {
                    quickSearchGridView.setVisibility(View.GONE);
                    quickAdd.setVisibility(View.GONE);
                    noSearchResult.setVisibility(View.GONE);
                    searchList.clear();
                    LogUtil.i("TAG", "cityimformations size is: " + cityInformations.size());
                    if (newText.length() > 1) {
                        for (int i = 0; i < cityInformations.size(); i++) {
                            String c = cityInformations.get(i).toString();
                            int index = c.indexOf(newText);
                            if (index != -1) {
                                searchList.add(c);
                            }
                        }
                        if (searchList.size() == 0) {
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
                    quickSearchGridView.setVisibility(View.VISIBLE);
                    quickAdd.setVisibility(View.VISIBLE);
                    cityListView.setVisibility(View.GONE);
                    noSearchResult.setVisibility(View.GONE);
                    searchList.clear();
                }
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {

        if (mSearchView.getQuery().length()>0){
            mSearchView.setQuery("", false);
            mSearchView.clearFocus();
        } else{
//            mSearchView.clearFocus();
            super.onBackPressed();
            closeProgressDialog();
            finish();
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
//                        boolean result = false;
//                        result = Utility.handleCityInfoResponse(weatherDB,response);
//                        if (result) {
//                            // 通过runonUiMainThread方法返回主线程处理逻辑
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
////                                    closeProgressDialog();
////                                    initCities();
//                                }
//                            });
//                        }
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


    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(this,R.style.MyProgressDialog);
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressDialog.setTitle("天气云");
            progressDialog.setMessage("初始化......");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setGravity(Gravity.CENTER);

            Window window = progressDialog.getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = 0.6f;
            lp.dimAmount = 0.1f;
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                    finish();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeProgressDialog();
        weatherDB.destroyDB();

    }

    /**
     *
     */
    private class InitCityInformationsTask extends AsyncTask<Void,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            while (true) {
                int progress = initCities();
//                publishProgress(progress);
                if (progress >= 2567) {
                    break;
                }
                return true;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

//            progressDialog.setMessage("初始化......" + values[0] * 100 / 2590 + "%");
            progressDialog.setProgress(values[0] * 100 / 2590);

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        /**
         * 从数据库加载全国城市信息
         */
        private int initCities() {
            int progress;
            myCityInformations = MyCityInformations.getInstance();
            cityInformations = myCityInformations.getCityInformations();
            LogUtil.i("TAG","cityinformations size is: " + cityInformations.size());
//            cityInformations = weatherDB.loadCityInformation();
            if ((cityInformations.size() < 2567)) {
                String response = j.getS1() + j.getS2() + j.getS3() +
                        j.getS4() + j.getS5() + j.getS6() + j.getS7() + j.getS8();
                response.replaceAll("\n", "");
                progress = queryFromDB(response);
            } else {
                progress = cityInformations.size();
            }
            return progress;
        }

        /**
         * 解析jsonresponse
         * @param response
         */
        private int queryFromDB( String response){
//            boolean result = false;
            int progress = 0;
//            progress = Utility.handleCityInfoResponse(weatherDB, response);

            if (!TextUtils.isEmpty(response)) {
                Gson gson = new Gson();
                AllCities allCities = gson.fromJson(response, AllCities.class);
                List<AllCities.CityInfo> cityList = allCities.getData();
                if (cityList != null && cityList.size() > 0) {
                    for (AllCities.CityInfo c : cityList) {
                        CityInformation cityInformation = new CityInformation();
                        cityInformation.setAreaid(c.getAreaid());
                        cityInformation.setDistrict(c.getDistrict());
                        cityInformation.setCity(c.getCity());
                        cityInformation.setProv(c.getProv());
                        LogUtil.i(TAG, "cityinfo is " + cityInformation.toString());

                        weatherDB.saveCityInformation(cityInformation);
                        progress++;
                        publishProgress(progress);
                    }
                }
            }

            if (progress == 2567) {
                myCityInformations.setCityInformations(weatherDB.loadCityInformation());
                initCities();
            }

            return progress;
        }



    }
}
