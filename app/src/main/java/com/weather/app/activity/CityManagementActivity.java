package com.weather.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.weather.app.R;
import com.weather.app.db.WeatherDB;
import com.weather.app.model.CityInformation;
import com.weather.app.util.MyApplication;
import com.weather.app.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * 城市管理界面
 * 添加,删除 城市
 * Created by zengpu on 15/10/31.
 */
public class CityManagementActivity extends BaseActivity {

    /**
     * 显示我的城市
     */
    private SwipeMenuListView cityManagment;

    private Toolbar mToolbar;

    /**
     * 添加城市
     */
    private Button addCity;

    private ArrayList<String> mCitiesList = new ArrayList<>();

    private List<CityInformation> mCities;

    private ArrayAdapter<String> cityManagementAdapter;

    private WeatherDB weatherDB;

    private boolean isCityListChangedFlag = false;

//    private RelativeLayout cityManagementView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_management_layout);

        weatherDB = WeatherDB.getInstance();
        initMyCities();

//        cityManagementView = (RelativeLayout) findViewById(R.id.city_management);
//        WindowManager wm = this.getWindowManager();
//        int height = wm.getDefaultDisplay().getHeight();
//
//        FrameLayout.LayoutParams layoutParams =
//                (FrameLayout.LayoutParams)cityManagementView.getLayoutParams();
//        layoutParams.height = height - Utility.getNavigationBarHeight(this);

        cityManagment = (SwipeMenuListView) findViewById(R.id.city_management_listView);
        mToolbar = (Toolbar) findViewById(R.id.city_management_Toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("城市管理");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.black045));

        addCity = (Button) findViewById(R.id.add_city);

        cityManagementAdapter = new ArrayAdapter<>(CityManagementActivity.this,
                R.layout.my_cities_item,mCitiesList);
        cityManagment.setAdapter(cityManagementAdapter);
        cityManagment.setVerticalScrollBarEnabled(false);

        cityManagment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SelectedCityWeatherActivity.actionStart(CityManagementActivity.this, position);
                finish();
            }
        });


        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu swipeMenu) {

                SwipeMenuItem deleteItem = new SwipeMenuItem(MyApplication.getContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xcf, 0xcf,
                        0xcf)));
                deleteItem.setWidth(Utility.dp2px(MyApplication.getContext(),70));
                deleteItem.setTitle("delete");
//                deleteItem.setIcon(R.mipmap.ic_plus3);
                deleteItem.setTitleSize(16);
                deleteItem.setTitleColor(Color.RED);

                swipeMenu.addMenuItem(deleteItem);
            }
        };

        cityManagment.setMenuCreator(creator);
        cityManagment.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu swipeMenu, int index) {

                switch (index) {
                    case 0:
                        mCitiesList.remove(position);
                        cityManagementAdapter.notifyDataSetChanged();
                        weatherDB.cancelSelectedCity(mCities.get(position));
                        isCityListChangedFlag = true;
                        break;
                }
                // true会关闭菜单,false不会关闭菜单
                return true;
            }
        });

        cityManagment.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int i) {

            }

            @Override
            public void onSwipeEnd(int i) {

            }
        });

        cityManagment.setOpenInterpolator(new BounceInterpolator());

        addCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchCityActivity.actionStart(CityManagementActivity.this, mCitiesList);
//                finish();
            }
        });
    }

    /**
     * 启动CityManagementActivity
     * @param context
     * @param currentCity
     */
    public static void actionStart(Context context,String currentCity){
        Intent intent = new Intent(context,CityManagementActivity.class);
        intent.putExtra("currentCity", currentCity);
        context.startActivity(intent);
    }

    /**
     * 从数据库中找到所有我的城市
     */
    private void initMyCities(){
        mCities =weatherDB.loadMyCities();

        if (mCities.size()==0){
            SearchCityActivity.actionStart(CityManagementActivity.this, mCitiesList);
        } else {

            for (int i=0;i<mCities.size();i++) {
                mCitiesList.add(mCities.get(i).getDistrict());
            }
        }
    }

    @Override
    public void onBackPressed() {

        //如果城市列表发生变化,刷新SelectedCityWeatherActivity
        if (isCityListChangedFlag) {
            int position = 0;
            Intent intent= getIntent();
            String currentCity = intent.getStringExtra("currentCity");
            for (int i =0;i < mCitiesList.size(); i++) {
                if (mCitiesList.get(i).equals(currentCity)) {
                    position = i;
                    break;
                } else {
                    position = 0;
                }
            }
            SelectedCityWeatherActivity.actionStart(CityManagementActivity.this, position);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //如果城市列表发生变化,刷新SelectedCityWeatherActivity
                if (isCityListChangedFlag) {
                    int position = 0;
                    Intent intent= getIntent();
                    String currentCity = intent.getStringExtra("currentCity");
                    for (int i =0;i < mCitiesList.size(); i++) {
                        if (mCitiesList.get(i).equals(currentCity)) {
                            position = i;
                            break;
                        } else {
                            position = 0;
                        }
                    }
                    SelectedCityWeatherActivity.actionStart(CityManagementActivity.this, position);
                    finish();
                } else {

                    Intent upIntent = NavUtils.getParentActivityIntent(this);
                    if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                        TaskStackBuilder.create(this)
                                .addNextIntentWithParentStack(upIntent)
                                .startActivities();
                    } else {
                        upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        NavUtils.navigateUpTo(this, upIntent);
                    }

                }
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        weatherDB.destroyDB();
    }
}
