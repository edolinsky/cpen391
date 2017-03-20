package cpen391.resty.resty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cpen391.resty.resty.ServerRequests.RestyMenuRequest;

public class MenuActivity extends AppCompatActivity {

    ArrayList<MenuItem> items;
    ListView menuListView;
    private static MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        String menuString = intent.getStringExtra(RestyMenuRequest.MENU);
        JsonParser parser = new JsonParser();
        JsonObject jsonMenu = (JsonObject)parser.parse(menuString);

        Gson gson = new Gson();
        Type menuListType = new TypeToken<List<MenuItem>>(){}.getType();
        List<MenuItem> menuList = gson.fromJson(jsonMenu.getAsJsonArray("items"), menuListType);

        items = new ArrayList<>();
        items.addAll(menuList);

        menuListView = (ListView)findViewById(R.id.menuList);
        adapter = new MenuItemAdapter(items, getApplicationContext());
        menuListView.setAdapter(adapter);
    }
}
