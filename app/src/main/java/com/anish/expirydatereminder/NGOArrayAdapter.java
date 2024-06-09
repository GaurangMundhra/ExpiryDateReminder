package com.anish.expirydatereminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NGOArrayAdapter extends ArrayAdapter<NGO> {

    public NGOArrayAdapter(Context context, List<NGO> ngos) {
        super(context, 0, ngos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        NGO ngo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ngo_item, parent, false);
        }

        ImageView ngoImage = convertView.findViewById(R.id.ngo_image);
        TextView ngoName = convertView.findViewById(R.id.ngo_name);

        ngoImage.setImageResource(ngo.getImageResourceId());
        ngoName.setText(ngo.getName());

        return convertView;
    }
}
