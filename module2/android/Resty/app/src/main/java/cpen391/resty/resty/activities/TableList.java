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

import java.util.ArrayList;

import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.Adapters.CustomList;

public class TableList extends AppCompatActivity {
    int tableCount = 7;
    ArrayList<String> printList = new ArrayList<String>();
    ListView list;
    Integer imageId = R.mipmap.burger_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Tables");

        setTableCount(tableCount);

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

    public void setTableCount(int i){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putInt("Tables", i);

        editor.commit();

    }

    public int getCount(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        int count = pref.getInt("count", 0);

        Log.d("COUNT: ", Integer.toString(count));
        return count;
    }

    public void addServer(int i, String order){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String newServer = order;
        editor.putString(Integer.toString(i), order);

        editor.commit();
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
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        element = pref.getString(Integer.toString(i), null);
        if(element == null){
            element = "";
        }
        return element;
    }


}