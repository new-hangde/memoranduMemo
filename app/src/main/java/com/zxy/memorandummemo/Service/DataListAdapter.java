package com.zxy.memorandummemo.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zxy.memorandummemo.page.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy on 2016/8/16.
 */
public class DataListAdapter extends ArrayAdapter<Data> {
    private int resourceId;
    public DataListAdapter(Context context,int TextViewResourceId,List<Data> object){
        super(context,TextViewResourceId,object);
        this.resourceId =TextViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Data data = getItem(position);
        LinearLayout dataListItem = new LinearLayout(getContext());
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater vi = (LayoutInflater)getContext().getSystemService(inflater);
        vi.inflate(resourceId, dataListItem, true);
        TextView content = (TextView)dataListItem.findViewById(R.id.content);
        TextView date = (TextView)dataListItem.findViewById(R.id.date);
        content.setText(data.getContent());
        date.setText(String.valueOf(data.getDate()));
        return dataListItem;
    }
}
