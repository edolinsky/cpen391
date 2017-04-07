package cpen391.resty.resty.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import cpen391.resty.resty.Objects.SingleMapping;
import cpen391.resty.resty.Objects.respTable;
import cpen391.resty.resty.Objects.tempTable;
import cpen391.resty.resty.R;
import cpen391.resty.resty.dataStore.RestyStore;
import cpen391.resty.resty.serverRequests.RestyOrderRequest;
import cpen391.resty.resty.serverRequests.RestyTableUpdateRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyTableCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyTableUpdateCallback;

/**
 * Created by anna on 2017-04-01.
 */

public class ListCheckExample extends AppCompatActivity {

    MyCustomAdapter dataAdapter = null;
    Intent intent;
    private RestyStore dataStore;
    String username;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_check_example);

        dataStore = RestyStore.getInstance();

        username = dataStore.getString(RestyStore.Key.USER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Edit your tables");

        intent = new Intent(this, TableList.class);
        //Generate list View from ArrayList
        displayListView();

        checkButtonClick();

    }

    private void displayListView() {

        //Array list of countries
        ArrayList<tempTable> countryList = new ArrayList<tempTable>();

        int count = dataStore.getInt("Tables");

        for(int i = 1; i <= count; i++){
            tempTable temp = new tempTable(i, "Table " + Integer.toString(i), false);
            countryList.add(temp);
        }

        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.row, countryList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                tempTable table = (tempTable) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + table.getServers(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private class MyCustomAdapter extends ArrayAdapter<tempTable> {

        private ArrayList<tempTable> countryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<tempTable> countryList) {
            super(context, textViewResourceId, countryList);
            this.countryList = new ArrayList<tempTable>();
            this.countryList.addAll(countryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.row, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        tempTable country = (tempTable) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        country.setSelected(cb.isChecked());

                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            tempTable country = countryList.get(position);

            holder.name.setText(country.getServers());
            holder.name.setChecked(country.isSelected());
            //holder.name.setChecked(getChecked(position));
            holder.name.setTag(country);
            if(getElement(position+1) != null) {
                String temp;
                temp = getElement(position+1);

                Log.d("Current servers:", temp);
                Log.d("Position", Integer.toString(position));
                Boolean b = temp.contains(username);
                if(temp.contains(username)){
                    holder.code.setText(" (You are currently a server for this table)");
                } else {
                    holder.code.setText(" (Not your table)");
                }

            } else{
                holder.code.setText(" (Not your table)");
            }
            return convertView;

        }

    }



    // Function that finds all the checked boxes
    private void checkButtonClick() {
        Button myButton = (Button) findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                ArrayList<tempTable> countryList = dataAdapter.countryList;
                for(int i=1;i<=countryList.size();i++){
                    tempTable country = countryList.get(i-1);
                    if(country.isSelected()) {

                        if(getElement(i) == null || getElement(i) == ""){
                            addServer(i, username);
                            Log.d("Test:", "In second else if");
                        }else if (getElement(i).contains(", " + username)) {
                            String temp = getElement(i);
                            temp = temp.replace(", " + username, "");

                            Log.d("Test:", "in first else if");

                            addServer(i, temp);
                            Log.d("String contents:", getElement(i));
                        } else if (getElement(i).contains(username)) {
                            String temp = getElement(i);
                            temp = temp.replace(username, "");
                            Log.d("Test:","In if");
                            addServer(i, temp);
                            Log.d("String contents::", getElement(i));

                        }else{
                            Log.d("test:", "in else");
                            String temp = getElement(i);
                            String temp1 = temp + ", " + username;

                            addServer(i, temp1);

                            Log.d("String contents::", getElement(i));
                        }


                    }
                }

                startActivity(intent);
            }
        });

    }

    public void addServer(int i, String order){
/*        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String newServer = order;
        editor.putString(Integer.toString(i), order);

        editor.commit();

        */
        dataStore.put(Integer.toString(i), order);
        putServer();
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

    private RestyTableUpdateCallback updateCallback = new RestyTableUpdateCallback() {
        @Override
        public void updateRetrieved(String table) {

        }

        @Override
        public void updateError(VolleyError error) {

        }
    };

    public void putServer(){
        RestyTableUpdateRequest orderRequest = new RestyTableUpdateRequest(updateCallback);

        int count = dataStore.getInt("Tables");
        List<SingleMapping> mapping = new ArrayList<SingleMapping>();

        for(int i = 0; i < count; i++){
            SingleMapping temp = new SingleMapping(dataStore.getString(Integer.toString(i)), "Table"+Integer.toString(i));
            mapping.add(temp);
        }

        orderRequest.changeServer(mapping);
    }

    public Boolean getChecked(int i){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        return pref.getBoolean("Boolean" + Integer.toString(i), false);
    }

    public void isItChecked(int i){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        editor.putBoolean("Boolean" + Integer.toString(i), !getChecked(i));

        editor.commit();
    }

    public int getCount(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        int count = pref.getInt("Tables", 0);

        Log.d("COUNT: ", Integer.toString(count));
        return count;
    }

}
