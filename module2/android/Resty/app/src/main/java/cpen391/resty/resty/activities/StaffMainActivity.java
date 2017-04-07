package cpen391.resty.resty.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import cpen391.resty.resty.Objects.RSOrder;
import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.Adapters.OrdersListViewAdapter;
import cpen391.resty.resty.serverRequests.RestyRSOrdersRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyOrdersPatchCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyRSOrdersCallback;

import static cpen391.resty.resty.Objects.RSOrder.groupAndSort;

public class StaffMainActivity extends AppCompatActivity {

    private ListView ordersView;
    private OrdersListViewAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        TextView topBar = (TextView) findViewById(R.id.StaffMainHeaderTextview);
        StaffUser user = (StaffUser) User.getCurrentUser();
        String title = "Welcome, " + user.getUser().split("@")[0];
        topBar.setText(title);
        topBar.setMaxLines(1);

        ordersView = (ListView) findViewById(R.id.StaffMainOredersListView);

        // fetch orders
        Runnable fetchOrders = new Runnable() {
            @Override
            public void run() {
                RestyRSOrdersRequest request = new RestyRSOrdersRequest(ordersCallback);
                request.getOrders(((StaffUser) User.getCurrentUser()).getRestaurant_id());
            }
        };

        Thread fetchOrdersThread = new Thread(fetchOrders);
        fetchOrdersThread.run();
    }

    private void displayOrders(ArrayList<RSOrder> orders){
        Log.i("COUNT", "" + orders.size());
        ArrayList<Object> groupedOrders = RSOrder.groupAndSort(orders);
        listAdapter = new OrdersListViewAdapter(groupedOrders, this, callback);
        ordersView.setAdapter(listAdapter);
    }

    public void manageTablesOnClick(View view){
        Intent intent = new Intent(this, TableList.class);
        startActivity(intent);
    }

    private RestyRSOrdersCallback ordersCallback = new RestyRSOrdersCallback() {
        @Override
        public void ordersRetrieved(ArrayList<RSOrder> orders) {
            displayOrders(orders);
        }

        @Override
        public void ordersError(VolleyError error) {

        }
    };

    public void updateDataset(){
        listAdapter.notifyDataSetChanged();
    }

    final RestyOrdersPatchCallback callback = new RestyOrdersPatchCallback() {
        @Override
        public void ordersUpdateSuccess(Activity activity) {
            if (activity != null){
                updateDataset();
                Toast toast = Toast.makeText(activity, "Order status updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        public void ordersUpdateError(VolleyError error) {
            Log.e("Patch Fail", "failure with patching dude: " + error.getMessage());

        }
    };
}
