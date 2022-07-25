package com.anish.expirydatereminder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogHandler.ExampleDialogListener, SettingsDialogHandler.ExampleDialogListener2, AdapterView.OnItemSelectedListener{

    private ArrayList<String> items;
    private ArrayAdapter<String> itemsAdapter;
    private ListView listView;
    private FloatingActionButton refreshButton, addItemButton, settingsButton;
    private Button sortButton;
    private DatabaseHandler dbHandler;
    private List<ItemModel> modelList;
    private final String[] categories = new String[]{"All Items","Grocery","Important dates","Medicine","Other Items"};
    SettingsDatabaseHandler settingsDatabaseHandler;

    private Spinner categorySpinner;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
        }

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        createNotificationChannel();

        listView = findViewById(R.id.listView);
        refreshButton = findViewById(R.id.refreshButton);
        addItemButton = findViewById(R.id.addItemButton);

        items = new ArrayList<>();
        itemsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, items);
        listView.setAdapter(itemsAdapter);

        setUpListViewListener();

        addItemButton.setOnClickListener(view -> openDialog());

        dbHandler = new DatabaseHandler(MainActivity.this);

        modelList = dbHandler.getAllItems();
        modelList.sort(Comparator.comparingInt(ItemModel::getMonth));
        modelList.sort(Comparator.comparingInt(ItemModel::getYear));
        populate(modelList);

        if(modelList.size()>0)
            setNotifications(0);
        else
            setNotifications(1);


        refreshButton.setOnClickListener(view -> {
            modelList = dbHandler.getAllItems();
            itemsAdapter.clear();
            modelList.sort(Comparator.comparingInt(ItemModel::getMonth));
            modelList.sort(Comparator.comparingInt(ItemModel::getYear));
            populate(modelList);
            itemsAdapter.notifyDataSetChanged();
        });


        categorySpinner = findViewById(R.id.category_spinner);
        categorySpinner.setOnItemSelectedListener(this);
        ArrayAdapter<String> ad = new ArrayAdapter<>(this, R.layout.spinner_item, categories);
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(ad);

        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(view -> openSettingsDialog());
        settingsDatabaseHandler = new SettingsDatabaseHandler(MainActivity.this);

        sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(view -> {
            String txt = sortButton.getText().toString();
            System.out.println("Button clicked");
            System.out.println(txt);
            if(txt.equals("Sort By: Date")){
                modelList = dbHandler.getAllItems();
                itemsAdapter.clear();
                modelList.sort(Comparator.comparing(ItemModel::getItem));
                populate(modelList);
                itemsAdapter.notifyDataSetChanged();
                sortButton.setText("Sort By: Name");
            }
            else if(txt.contains("Sort By: Name")){
                modelList = dbHandler.getAllItems();
                itemsAdapter.clear();
                modelList.sort(Comparator.comparingInt(ItemModel::getMonth));
                modelList.sort(Comparator.comparingInt(ItemModel::getYear));
                populate(modelList);
                itemsAdapter.notifyDataSetChanged();
                sortButton.setText("Sort By: Date");
            }
        });

    }


    private void populate(List<ItemModel> list) {
        for(ItemModel a:list){
            addItem(a);
        }
    }

    private void setUpListViewListener() {
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder altdial = new AlertDialog.Builder(MainActivity.this);
            altdial.setMessage("Do you want to delete "+items.get(i)+" ???").setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Context context = getApplicationContext();
                        Toast.makeText(context,"Item Removed",Toast.LENGTH_SHORT).show();
                        items.remove(i);
                        System.out.println(modelList.get(i).getItem());
                        dbHandler.deleteRow(modelList.get(i));
                        itemsAdapter.notifyDataSetChanged();

                        String fileNameText = "anish_" + modelList.get(i).getItem() + "." + modelList.get(i).getMonth() + "." + modelList.get(i).getYear() + ".jpg";
                        Uri uri = Uri.parse("content://com.anish.expirydatereminder.provider/cache/images/"+fileNameText);
                        ContentResolver contentResolver = getContentResolver();
                        contentResolver.delete(uri,null,null);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel());

            AlertDialog alert = altdial.create();
            alert.setTitle("Delete this item?");
            alert.show();
            return true;
        });
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent myIntent = new Intent(MainActivity.this, ItemDetailsOnClick.class);
            myIntent.putExtra("item name",modelList.get(i).getItem());
            myIntent.putExtra("month",modelList.get(i).getMonth());
            myIntent.putExtra("year",modelList.get(i).getYear());
            myIntent.putExtra("date",modelList.get(i).getDate());
            myIntent.putExtra("category",modelList.get(i).getCategory());
            MainActivity.this.startActivity(myIntent);
        });
    }

    private int checkIfItemExists(String item, int month, int year, int date, String category) {
        List<ItemModel> list_of_items = dbHandler.getAllItems();
        for (ItemModel obj:list_of_items){
            if(obj.getItem().equals(item)){
                if(obj.getMonth() == month){
                    if(obj.getYear() == year){
                        return 3;
                    }
                }
                if(year >= obj.getYear()) {
                    return 2;
                }
            }
        }
        return 1;
    }

    /*private void addItem() {
        EditText input = findViewById(R.id.itemName);
        EditText m = findViewById(R.id.month);
        EditText y = findViewById(R.id.year);


        String itemName = input.getText().toString();

        if(!itemName.isEmpty() && !m.getText().toString().isEmpty() && !y.getText().toString().isEmpty()){
            int month = Integer.parseInt(m.getText().toString());
            int year  = Integer.parseInt(y.getText().toString());

            if(month > 12 || month<0){
                Toast.makeText(getApplicationContext(),"Incorrect month input!!!",Toast.LENGTH_SHORT).show();
                m.setText("");
                return;
            }

            if(year < 1000 || year > 9999){
                Toast.makeText(getApplicationContext(),"Please enter year in YYYY format",Toast.LENGTH_SHORT).show();
                y.setText("");
                return;
            }

            int checker = checkIfItemExists(itemName,month,year);

            if(checker == 3){
                Toast.makeText(getApplicationContext(),"This item already exists!",Toast.LENGTH_SHORT).show();
                input.setText("");
                y.setText("");
                m.setText("");
                return;
            }
            else if(checker == 2){
                Toast.makeText(getApplicationContext(),"Same Item with different expiry date exists! ",Toast.LENGTH_SHORT).show();
            }


            String text = month + "/" + year + " : " + itemName;

            itemsAdapter.add(text);
            dbHandler.addNewItem(new ItemModel(itemName,month,year));
            modelList.add(new ItemModel(itemName,month,year));

            input.setText("");
            m.setText("");
            y.setText("");
        }
        else{
            Toast.makeText(getApplicationContext(),"Fields are empty!!!",Toast.LENGTH_SHORT).show();
        }
    }*/
    private void addItem(ItemModel obj) {
        String itemName = obj.getItem();
        int month = obj.getMonth();
        int year  = obj.getYear();
        int date = obj.getDate();
        String totalItem = month+"/"+ date+"/"+year + " : " + itemName;
        if(!itemName.isEmpty()){
            itemsAdapter.add(totalItem);
        }
        else{
            Toast.makeText(getApplicationContext(),"Cannot be empty string, enter text",Toast.LENGTH_SHORT).show();
        }
    }
    private void addItem(String itemName,int date, int month, int year, String category){
        int checker = checkIfItemExists(itemName,month,year,date,category);

        if(checker == 3){
            Toast.makeText(getApplicationContext(),"This item already exists!",Toast.LENGTH_SHORT).show();
            return;
        }
        else if(checker == 2){
            Toast.makeText(getApplicationContext(),"Same Item with different expiry date exists! Finish that first! ",Toast.LENGTH_SHORT).show();
            return;
        }

        String text = month + "/" + date + "/" + year + " : " + itemName;

        itemsAdapter.add(text);
        dbHandler.addNewItem(new ItemModel(itemName,month,year,date,category));
        modelList.add(new ItemModel(itemName,month,year,date,category));
    }

    private void createNotificationChannel(){
        CharSequence name = "EDR Channel";
        String description = "Channel for Expiry Date Reminder notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("edr_channel_1",name,importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
    private void setNotifications(int a){
        Intent intent = new Intent(MainActivity.this,NotificationHandler.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent,PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 8);
        alarmStartTime.set(Calendar.MINUTE, 30);
        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            Log.d("Hey","Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
        Log.d("Alarm","Alarms set for everyday 8:30 am and pm.");
        if(a==1){
            alarmManager.cancel(pendingIntent);
        }
    }

    private void openDialog() {
        DialogHandler dialogHandler = new DialogHandler();
        dialogHandler.show(getSupportFragmentManager(),"Add item");
    }
    private void openSettingsDialog(){
        SettingsDialogHandler settingsDialogHandler = new SettingsDialogHandler();
        settingsDialogHandler.show(getSupportFragmentManager(),"Settings");
    }

    @Override
    public void addItemAsNeeded(String item_name, int date, int month, int year, String category_name) {
        addItem(item_name,date,month,year,category_name);
        itemsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(i==0){
            if(sortButton.getText().toString().equals("Sort By: Date")){
                modelList = dbHandler.getAllItems();
                itemsAdapter.clear();
                modelList.sort(Comparator.comparingInt(ItemModel::getMonth));
                modelList.sort(Comparator.comparingInt(ItemModel::getYear));
                populate(modelList);
                itemsAdapter.notifyDataSetChanged();
            }
            else {
                modelList = dbHandler.getAllItems();
                itemsAdapter.clear();
                modelList.sort(Comparator.comparing(ItemModel::getItem));
                populate(modelList);
                itemsAdapter.notifyDataSetChanged();
            }
        }
        else {
            if(sortButton.getText().toString().equals("Sort By: Date")){
                modelList = dbHandler.getAllItems(categories[i]);
                itemsAdapter.clear();
                modelList.sort(Comparator.comparingInt(ItemModel::getMonth));
                modelList.sort(Comparator.comparingInt(ItemModel::getYear));
                populate(modelList);
                itemsAdapter.notifyDataSetChanged();
            }
            else {
                modelList = dbHandler.getAllItems(categories[i]);
                itemsAdapter.clear();
                modelList.sort(Comparator.comparing(ItemModel::getItem));
                populate(modelList);
                itemsAdapter.notifyDataSetChanged();
            }
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }











    @Override
    public void addCategory(String categoryName) {

    }

    @Override
    public int deleteCategory(String category_name) {
        return 0;
    }

    @Override
    public void restoreDefault() {

    }

    @Override
    public List<String> getCategories() {
        return null;
    }
}


//https://stackoverflow.com/questions/19699776/write-multiline-text-on-button-in-android
//https://stackoverflow.com/questions/13624442/getting-last-day-of-the-month-in-a-given-string-date/40689365#40689365