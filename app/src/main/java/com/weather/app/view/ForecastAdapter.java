package com.weather.app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.app.R;
import com.weather.app.util.Utility;

import java.util.ArrayList;

/**
 * Created by zengpu on 15/11/19.
 */
public class ForecastAdapter extends BaseAdapter{

    private ArrayList<String> dateList = new ArrayList<String>();
    private ArrayList<String> mCodes = new ArrayList<String>();
    private Context context;
    private LayoutInflater mInflater;


    public ForecastAdapter(Context context, ArrayList<String> dateList,
                           ArrayList<String> mCodeList) {

        this.dateList = dateList;
        this.mCodes = mCodeList;
        this.context = context;
        mInflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return dateList.size();
    }

    @Override
    public Object getItem(int position) {
        return dateList.get(position);
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
            view = mInflater.inflate(R.layout.forecast_item,null);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) view.findViewById(R.id.date);
            viewHolder.condImag = (ImageView) view.findViewById(R.id.weather_imag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        if (position == 0) {
            viewHolder.date.setText("今天");
        } else {
            viewHolder.date.setText(dateList.get(position));
        }
        Utility.setCondImage(mCodes.get(position),viewHolder.condImag);
//        viewHolder.condImag.setColorFilter(R.color.weather_image_icon_tint_color);
        return view;
    }

    class ViewHolder {
        TextView date;
        ImageView condImag;
    }
}
