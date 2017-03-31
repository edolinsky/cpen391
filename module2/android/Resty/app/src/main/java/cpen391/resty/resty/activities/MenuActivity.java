package cpen391.resty.resty.activities;

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

import cpen391.resty.resty.menu.MenuItem;
import cpen391.resty.resty.menu.MenuItemAdapter;
import cpen391.resty.resty.R;
import cpen391.resty.resty.serverRequests.RestyMenuRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyMenuCallback;
import cpen391.resty.resty.utils.TestDataUtils;

public class MenuActivity extends AppCompatActivity {

    ArrayList<MenuItem> items;
    ListView menuListView;
    private static MenuItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuListView = (ListView)findViewById(R.id.menuList);

        RestyMenuRequest menuRequest = new RestyMenuRequest(callback);
        menuRequest.getMenu(TestDataUtils.TEST_RESTAURANT, null);
    }

    public void onMenuFetchSuccess(String menuString){
        JsonParser parser = new JsonParser();
        JsonObject jsonMenu = (JsonObject)parser.parse(menuString);

        Gson gson = new Gson();
        Type menuListType = new TypeToken<List<MenuItem>>(){}.getType();
        List<MenuItem> menuList = gson.fromJson(jsonMenu.getAsJsonArray("items"), menuListType);

        items = new ArrayList<>();
        items.addAll(menuList);

        adapter = new MenuItemAdapter(items, getApplicationContext());
        menuListView.setAdapter(adapter);
    }


    private void onFetchMenuError(RestyMenuCallback.FetchMenuError error){
        switch (error){
            case UnknownError:
                break;
            default:
                break;
        }
    }

    private RestyMenuCallback callback = new RestyMenuCallback() {
        @Override
        public void fetchMenuSuccess(String menu) {
            onMenuFetchSuccess(menu);
        }

        @Override
        public void fetchMenuError(FetchMenuError error) {
            onFetchMenuError(error);
        }
    };
}
