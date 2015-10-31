package com.weather.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.weather.app.R;
import com.weather.app.util.Utility;

import java.util.List;

/**
 * 快速搜索Gridview的adapter
 * Created by zengpu on 15/12/6.
 */
public class QuickSearchViewAdapter extends ArrayAdapter<String> {

    private int resource;
    private LayoutInflater mInflater;
    private List<String> citylist;
    private Context context;

    public QuickSearchViewAdapter(Context context, int resource, String[] objects, List<String> citylist) {
        super(context, resource, objects);
        this.resource = resource;
        this.citylist = citylist;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String cityName = getItem(position);

        View view = mInflater.inflate(resource, null);

        TextView itemCityName = (TextView) view.findViewById(R.id.item_city_name);
//        ImageView isSelected = (ImageView) view.findViewById(R.id.selected);
        itemCityName.setText(cityName);

        if (citylist != null) {
            for (int i = 0; i< citylist.size(); i++) {
                if (cityName.equals(citylist.get(i).toString())) {
                    int paddingUpBottom = Utility.dp2px(context, 13);
                    int paddingLeftRight = Utility.dp2px(context, 25);
                    itemCityName.setPadding(paddingLeftRight,paddingUpBottom,
                                            paddingLeftRight,paddingUpBottom);
                    itemCityName.setTextColor(context.getResources().getColor(R.color.city_name_color));
                    Drawable isSlected = context.getResources().getDrawable(R.mipmap.ic_check);
                    isSlected.setBounds(0, 0, isSlected.getMinimumWidth(), isSlected.getMinimumHeight());
                    itemCityName.setCompoundDrawables(null, null, isSlected, null);
                    break;
                }
            }
        }
        return view;
    }
}
