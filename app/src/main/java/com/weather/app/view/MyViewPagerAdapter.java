package com.weather.app.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by zengpu on 15/11/2.
 */
public class MyViewPagerAdapter extends PagerAdapter {

    private int mChildCount = 0;

    private List<View> viewList;

    public MyViewPagerAdapter(List<View> viewList) {
        this.viewList = viewList;//构造方法，参数是我们的页卡，这样比较方便。
    }

    @Override
    public int getCount() {
        return  viewList.size();//返回页卡的数量
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡

        container.addView(viewList.get(position));//添加页卡
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object)   {
        container.removeView(viewList.get(position));//删除页卡
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写
    }

    @Override
    public int getItemPosition(Object object) {
//
//        if ( mChildCount > 0) {
//            mChildCount --;
//            return POSITION_NONE;
//        }

        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
//        mChildCount = getCount();
        super.notifyDataSetChanged();
    }
}
