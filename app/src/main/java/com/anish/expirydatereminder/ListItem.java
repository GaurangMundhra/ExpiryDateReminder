package com.anish.expirydatereminder;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

public class ListItem extends AppCompatActivity implements DialogHandler.ExampleDialogListener,
        SettingsDialogHandler.SettingsDialog {

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private FloatingActionButton refreshButton, addItemButton;
    private Button sortButton;
    private FloatingActionButton settingsButton, helpButton;

    private DatabaseHandler dbHandler;
    private List<ItemModel> modelList = new ArrayList<>(); // Initialize modelList here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        listView = findViewById(R.id.listView);
        refreshButton = findViewById(R.id.refreshButton);
        addItemButton = findViewById(R.id.addItemButton);
        settingsButton = findViewById(R.id.settings_button);
        helpButton = findViewById(R.id.help_button);

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);

        addItemButton.setOnClickListener(view -> openDialog());

        dbHandler = new DatabaseHandler(this);

        loadItemsFromDatabase();

        refreshButton.setOnClickListener(view -> {
            modelList.clear();
            itemsAdapter.clear();
            loadItemsFromDatabase();
        });

        sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(view -> {
            if (sortButton.getText().toString().equals("Sort By: Name")) {
                sortItemsByName();
            } else {
                sortItemsByDate();
            }
        });

        settingsButton.setOnClickListener(view -> openSettingsDialog());

        helpButton.setOnClickListener(view -> {
            HelpDialogHandler hd = new HelpDialogHandler();
            hd.show(getSupportFragmentManager(), "Help");
        });

        setAlarm();
    }

    private void loadItemsFromDatabase() {
        modelList = dbHandler.getAllItems();
        populateList(modelList);
    }

    private void populateList(List<ItemModel> itemList) {
        for (ItemModel item : itemList) {
            addItemToListView(item);
        }
    }

    private void addItemToListView(ItemModel item) {
        String itemText = item.getItem() + "\n" + item.getDate() + "/" + item.getMonth() + "/" + item.getYear();
        items.add(itemText);
        itemsAdapter.notifyDataSetChanged();
    }

    private void openDialog() {
        DialogHandler dialog = new DialogHandler();
        dialog.show(getSupportFragmentManager(), "DialogHandler");
    }

    private void sortItemsByName() {
        modelList.sort(Comparator.comparing(ItemModel::getItem));
        updateListView();
        sortButton.setText("Sort By: Date");
    }

    private void sortItemsByDate() {
        modelList.sort(Comparator.comparingInt(ItemModel::getYear)
                .thenComparingInt(ItemModel::getMonth)
                .thenComparingInt(ItemModel::getDate));
        updateListView();
        sortButton.setText("Sort By: Name");
    }

    private void updateListView() {
        items.clear();
        populateList(modelList);
    }

    private void setAlarm() {
        AlarmManager alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, WakeUpReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, alarmIntent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void addItemAsNeeded(String item, int year, int month, int day, String category) {
        ItemModel newItem = new ItemModel(item, year, month, day, category);
        dbHandler.addNewItem(newItem);
        modelList.add(newItem);
        addItemToListView(newItem);
    }

    @Override
    public void refresh(int a) {
        // Not needed for this implementation
    }

    @Override
    public void deleteImages(String category) {
        // Not needed for this implementation
    }

    @Override
    public void deleteImages() {
        // Not needed for this implementation
    }

    private void openSettingsDialog() {
        SettingsDialogHandler settingsDialog = new SettingsDialogHandler();
        settingsDialog.show(getSupportFragmentManager(), "SettingsDialogHandler");
    }
}
