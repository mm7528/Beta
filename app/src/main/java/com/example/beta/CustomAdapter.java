package com.example.beta;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StorageReference> images;
    private ArrayList<String>  stringsList;
    private LayoutInflater inflater;


    public CustomAdapter(MyRecipes context) {
        this.context = context;
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

    public void setStringsList(ArrayList<String> stringsList) {
        this.stringsList = stringsList;
    }

    public void setImages(ArrayList<StorageReference> images)
    {
        this.images=images;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.custom_lv_layout, parent, false);
        ImageView img = (ImageView) view.findViewById(R.id.iV);
        TextView str = (TextView) view.findViewById(R.id.tV);
        str.setText(stringsList.get(position));
        images.get(position).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(img);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
        return view;
    }
}