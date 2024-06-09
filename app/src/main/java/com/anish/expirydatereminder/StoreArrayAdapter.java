package com.anish.expirydatereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StoreArrayAdapter extends ArrayAdapter<Store> {

    public StoreArrayAdapter(Context context, List<Store> stores) {
        super(context, 0, stores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Store store = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_item, parent, false);
        }

        ImageView storeImage = convertView.findViewById(R.id.store_image);
        TextView storeName = convertView.findViewById(R.id.store_name);

        storeImage.setImageResource(store.getImageResourceId());
        storeName.setText(store.getName());

        return convertView;
    }
}
