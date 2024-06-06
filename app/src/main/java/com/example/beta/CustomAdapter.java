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

/**
 * The type Custom adapter.
 */
public class CustomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<StorageReference> images;
    private ArrayList<String>  stringsList;
    private LayoutInflater inflater;


    /**
     * Instantiates a new Custom adapter.
     *
     * @param context the context
     */
    public CustomAdapter(MyRecipes context) {
        this.context = context;
        inflater = (LayoutInflater.from(context));
    }

    /**
     * gets the amount of the recipes
     * @return the amount of the recipes
     */
    @Override
    public int getCount() {
        return stringsList.size();
    }

    /**
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return the title of the recipe in this position
     */
    @Override
    public Object getItem(int position) {
        return stringsList.get(position);
    }

    /**
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return the position of the recipe in this position
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Sets strings list.
     *
     * @param stringsList the strings list
     */
    public void setStringsList(ArrayList<String> stringsList) {
        this.stringsList = stringsList;
    }

    /**
     * Sets images.
     *
     * @param images the images
     */
    public void setImages(ArrayList<StorageReference> images)
    {
        this.images=images;
    }

    /**
     * the function defines each item in the ListView, presenting the title and the pictures
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param view The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
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