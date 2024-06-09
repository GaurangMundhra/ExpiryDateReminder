package com.anish.expirydatereminder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class StoresActivity extends AppCompatActivity {

    private ListView listView;
    private StoreArrayAdapter storeArrayAdapter;
    private List<Store> storeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        listView = findViewById(R.id.listView);

        storeList = new ArrayList<>();
        // Add some stores to the list
        storeList.add(new Store("Store 1", R.drawable.store1_image));
        storeList.add(new Store("Store 2", R.drawable.store2_image));
        // Add more stores as needed

        storeArrayAdapter = new StoreArrayAdapter(this, storeList);
        listView.setAdapter(storeArrayAdapter);
    }
}

