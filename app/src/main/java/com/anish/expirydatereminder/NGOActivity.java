package com.anish.expirydatereminder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NGOActivity extends AppCompatActivity {

    private ListView listView;
    private NGOArrayAdapter ngoArrayAdapter;
    private List<NGO> ngoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo);

        listView = findViewById(R.id.listView);

        ngoList = new ArrayList<>();
        // Add some NGOs to the list
        ngoList.add(new NGO("NGO 1", R.drawable.ngo1_image));
        ngoList.add(new NGO("NGO 2", R.drawable.ngo2_image));
        // Add more NGOs as needed

        ngoArrayAdapter = new NGOArrayAdapter(this, ngoList);
        listView.setAdapter(ngoArrayAdapter);
    }
}
