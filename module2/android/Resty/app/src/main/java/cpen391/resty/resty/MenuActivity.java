package cpen391.resty.resty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    MenuItem items;
    ListView menuListView;
    private static MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String jsonMenu = intent.getStringExtra(HubAuthenticationActivity.MENU);

        Gson gson = new Gson();
        items = gson.fromJson(jsonMenu, MenuItem.class);

        menuListView = (ListView)findViewById(R.id.menuList);

        ArrayList<MenuItem> menu = new ArrayList<>();
        menu.add(items);
        adapter = new MenuItemAdapter(menu, getApplicationContext());
        menuListView.setAdapter(adapter);
    }
}
