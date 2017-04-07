package cpen391.resty.resty.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.Table;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.Objects.respTable;
import cpen391.resty.resty.Objects.tempTable;
import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.Adapters.CustomList;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.menu.MenuItemAdapter;
import cpen391.resty.resty.serverRequests.RestyTableRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyTableCallback;

public class TableList extends AppCompatActivity {
    //int tableCount = 7;
    ArrayList<String> printList = new ArrayList<String>();
    ListView list;
    Integer imageId = R.mipmap.burger_icon;
    CustomList adap;
    private RestyStore dataStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dataStore = RestyStore.getInstance();

        setTitle("Tables");


        // START OF SERVER
        Runnable fetchOrders = new Runnable() {
            @Override
            public void run() {
                RestyTableRequest request = new RestyTableRequest(tablesCallback);
                request.getTables("test_resto");
                // ABOVE LINE IS BAD
            }
        };

        Thread fetchTablesThread = new Thread(fetchOrders);
        fetchTablesThread.run();

        // END OF SERVER

        //setTableCount(tableCount);
        int tableCount = dataStore.getInt("Tables");

        Log.d("Current count", Integer.toString(tableCount));

        for (int i = 1; i <= tableCount; i++) {
            //String temp = getElement(i);
            String temp = "Table " + Integer.toString(i) + "\nServers:" + getElement(i);
            Log.d("Value", temp);
            if (temp == null) {
                Log.d("AW SHIT: ", "shit broke");
            }

            printList.add(temp);
        }

        final String [] finalList = printList.toArray(new String[printList.size()]);

        CustomList adapter = new
                CustomList(TableList.this, finalList, imageId);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(TableList.this, "This is your " + id + "th order"/*finalList[+ position]*/,
                        Toast.LENGTH_SHORT).show();

            }
        });
    }



    private RestyTableCallback tablesCallback = new RestyTableCallback() {
        @Override
        public void tablesRetrieved(ArrayList<respTable> table) {

        }

        @Override
        public void tablesError(VolleyError error) {

        }
    };




    public void setTableCount(int i){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("Tables", i);

        editor.commit();

    }

    public int getCount(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        int count = pref.getInt("Tables", 0);

        Log.d("COUNT: ", Integer.toString(count));
        return count;
    }


    public void testList(View view){
        Intent intent = new Intent(this, ListCheckExample.class);

        startActivity(intent);
    }

    public void clear(View view) {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("count", 0);

        editor.clear();
        editor.commit();

        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    public String getElement(int i){
        String element;

        element = dataStore.getString(Integer.toString(i), null);
        //Log.d("servers", element);
        if(element == null){
            element = "";
        }
        return element;
    }


}