package com.weather.app.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.weather.app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zengpu on 15/11/22.
 */
public class SuggestionAdapter extends BaseAdapter {

    private List<String> itemName = new ArrayList<>();
    private List<String> itemContent = new ArrayList<>();
    private List<Drawable> itemImages = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;


    public SuggestionAdapter(Context context, List<String> itemName,
                                   List<String> itemContent, List<Drawable> itemImages) {

        this.itemName = itemName;
        this.itemContent = itemContent;
        this.itemImages = itemImages;
        this.context = context;
        mInflater = LayoutInflater.from(this.context);

    }

    @Override
    public int getCount() {
        return itemName.size();
    }

    @Override
    public Object getItem(int position) {
        return itemName.get(position);
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
            view = mInflater.inflate(R.layout.suggestion_item,null);
            viewHolder = new ViewHolder();
//            viewHolder.itemName = (TextView) view.findViewById(R.id.item_name);
            viewHolder.itemContent = (TextView) view.findViewById(R.id.item_content);
            viewHolder.itemImag = (ImageView) view.findViewById(R.id.item_imag);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

//        viewHolder.itemName.setText(itemName.get(position));
        viewHolder.itemContent.setText(itemContent.get(position));
        viewHolder.itemImag.setImageDrawable(itemImages.get(position));
        return view;
    }

    class ViewHolder {
//        TextView itemName;
        TextView itemContent;
        ImageView itemImag;
    }


}
