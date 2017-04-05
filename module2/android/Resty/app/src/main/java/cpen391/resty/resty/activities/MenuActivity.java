package cpen391.resty.resty.activities;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import cpen391.resty.resty.Objects.Table;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.menu.OrderDialog;
import cpen391.resty.resty.menu.RestaurantMenuItem;
import cpen391.resty.resty.menu.MenuItemAdapter;
import cpen391.resty.resty.R;
import cpen391.resty.resty.serverRequests.RestyMenuRequest;
import cpen391.resty.resty.serverRequests.RestyOrderRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyMenuCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyOrderCallback;
import cpen391.resty.resty.utils.TestDataUtils;

public class MenuActivity extends MainActivityBase implements OrderDialog.OrderDialogListener{

    ArrayList<RestaurantMenuItem> items;
    ListView menuListView;
    Table connectedTable;
    String userId;
    private static MenuItemAdapter adapter;

    private RestyMenuCallback menuCallback = new RestyMenuCallback() {
        @Override
        public void fetchMenuSuccess(String menu) {
            onMenuFetchSuccess(menu);
        }

        @Override
        public void fetchMenuError(VolleyError error) {
            onFetchMenuError(error);
        }
    };

    private RestyOrderCallback orderCallback = new RestyOrderCallback() {
        @Override
        public void orderComplete() {
            for(RestaurantMenuItem i : items) {
                i.reset();
            }
            adapter.notifyDataSetChanged();

            Toast orderCompleteToast = Toast.makeText(getApplicationContext(), "Order sent!",
                    Toast.LENGTH_LONG);
            orderCompleteToast.show();
        }

        @Override
        public void orderError(VolleyError error) {
            Toast orderFailureToast = Toast.makeText(getApplicationContext(),
                    "There was a problem with sending your order, please try again.",
                    Toast.LENGTH_LONG);
            orderFailureToast.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menuListView = (ListView)findViewById(R.id.menuList);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RestyMenuRequest menuRequest = new RestyMenuRequest(menuCallback);

        connectedTable = new Table(TestDataUtils.TEST_RESTAURANT, TestDataUtils.TEST_TABLE);
        userId = TestDataUtils.TEST_USER;
        menuRequest.getMenu(connectedTable.getRestaurantId(), null);
    }


    public void onMenuFetchSuccess(String menuString){
        JsonParser parser = new JsonParser();
        JsonObject jsonMenu = (JsonObject)parser.parse(menuString);

        Gson gson = new Gson();
        Type menuListType = new TypeToken<List<RestaurantMenuItem>>(){}.getType();
        List<RestaurantMenuItem> menuList = gson.fromJson(jsonMenu.getAsJsonArray("items"), menuListType);

        items = new ArrayList<>();
        items.addAll(menuList);

        adapter = new MenuItemAdapter(items, getApplicationContext());
        menuListView.setAdapter(adapter);
    }

    private void onFetchMenuError(VolleyError error){
        switch (error.networkResponse.statusCode){
            case 500:
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
        public void fetchMenuError(VolleyError error) {
            onFetchMenuError(error);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_shopping_cart:

                OrderDialog order = new OrderDialog();
                order.show(getSupportFragmentManager(), "Order Dialog");

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onDialogConfirmation(DialogFragment dialog) {
        List<RestaurantMenuItem> order = new ArrayList<>();

        // collect items in shopping cart
        for(RestaurantMenuItem i : items) {
            if(i.getAmount() != 0) {
                order.add(i);
            }
        }

        RestyOrderRequest orderRequest = new RestyOrderRequest(orderCallback);
        orderRequest.order(order, userId, connectedTable);
    }

    @Override
    public void onDialogCancellation(DialogFragment dialog) {
        // nothing
    }
}
