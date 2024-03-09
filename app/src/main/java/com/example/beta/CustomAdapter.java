package com.example.beta;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private int images[];
    private ArrayList<String>  stringsList;
    private LayoutInflater inflater;

    public CustomAdapter(Context context, int[] images, ArrayList<String> stringsList) {
        this.context = context;
        this.images = images;
        this.stringsList = stringsList;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return stringsList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.custom_lv_layout, parent, false);
        ImageView img = (ImageView) view.findViewById(R.id.iV);
        TextView str = (TextView) view.findViewById(R.id.tV);
        str.setText(stringsList.get(position));
        img.setImageResource(images[position]);
        return view;
    }
}
