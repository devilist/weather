package com.weather.app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.weather.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengpu on 15/11/21.
 */
public class HourlyForecastAdapter extends BaseAdapter {

    private List<String> hourList = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;


    public HourlyForecastAdapter(Context context, List<String> hourList) {

        this.hourList = hourList;
        this.context = context;
        mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return hourList.size();
    }

    @Override
    public Object getItem(int position) {
        return hourList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = mInflater.inflate(R.layout.hourly_forecast_item,null);
            viewHolder = new ViewHolder();
            viewHolder.hour = (TextView) view.findViewById(R.id.hour);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.hour.setText(hourList.get(position));
        return view;
    }

    class ViewHolder {
        TextView hour;
    }
}
