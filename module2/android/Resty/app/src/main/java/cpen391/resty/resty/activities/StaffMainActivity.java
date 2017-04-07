package cpen391.resty.resty.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import cpen391.resty.resty.Objects.RSOrder;
import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.Objects.respTable;
import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.Adapters.OrdersListViewAdapter;
import cpen391.resty.resty.activities.Fragments.StaffOrderStatusDialog;
import cpen391.resty.resty.serverRequests.RestyRSOrdersRequest;

import cpen391.resty.resty.serverRequests.RestyTableRequest;

import cpen391.resty.resty.serverRequests.serverCallbacks.RestyOrdersPatchCallback;

import cpen391.resty.resty.serverRequests.serverCallbacks.RestyRSOrdersCallback;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyTableCallback;

import static android.R.attr.checked;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static cpen391.resty.resty.Objects.RSOrder.groupAndSort;

public class StaffMainActivity extends AppCompatActivity {

    private static final String TAG = "StaffMain";
    private ListView ordersView;
    private OrdersListViewAdapter listAdapter;
    private final AtomicBoolean selectModeActivated = new AtomicBoolean(false);
    private final ArrayList<RSOrder> selectedOrders = new ArrayList<RSOrder>();
    private Runnable fetchOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_main);

        // Display welcome message with user's email mailbox name.
        TextView topBar = (TextView) findViewById(R.id.StaffMainHeaderTextview);
        StaffUser user = (StaffUser) User.getCurrentUser();
        String title = "Welcome " + user.getUser().split("@")[0];
        topBar.setText(title);
        topBar.setMaxLines(1);

        ordersView = (ListView) findViewById(R.id.StaffMainOredersListView);

        // Preload the tables
        Runnable fetchTables = new Runnable() {
            @Override
            public void run() {
                RestyTableRequest request = new RestyTableRequest(tablesCallback);
                request.getTables("test_resto");
                // ABOVE LINE IS BAD
            }
        };

        Thread fetchTablesThread = new Thread(fetchTables);
        fetchTablesThread.run();

        // fetch orders
        fetchOrders = new Runnable() {
            @Override
            public void run() {
                RestyRSOrdersRequest request = new RestyRSOrdersRequest(ordersCallback);
                request.getOrders(((StaffUser) User.getCurrentUser()).getRestaurant_id());
            }
        };

        Thread fetchOrdersThread = new Thread(fetchOrders);
        fetchOrdersThread.run();


    }

    public ListView getOrdersView() {
        return ordersView;
    }

    public boolean selectModeActivated(){
        return selectModeActivated.get();
    }

    private void displayOrders(ArrayList<RSOrder> orders){
        selectedOrders.clear();
        ArrayList<Object> groupedOrders = RSOrder.groupAndSort(orders);

        listAdapter = new OrdersListViewAdapter(groupedOrders, this, selectedOrders, callback);
        ordersView.setAdapter(listAdapter);

        final OrdersListViewAdapter adapter = listAdapter;
        final ListView list = ordersView;

        // Select mode button
        final Button selectModeButton = (Button) findViewById(R.id.RSOrderSelectModeButton);
        final Button editButton = (Button) findViewById(R.id.RSOrderEditButton);
        selectModeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectModeActivated.get()){
                    selectModeActivated.set(false);
                    editButton.setVisibility(View.INVISIBLE);
                    selectModeButton.setText("Select");
                }else{
                    selectModeActivated.set(true);
                    editButton.setVisibility(View.VISIBLE);
                    selectModeButton.setText("Cancel");
                }
                adapter.toggleCheckBoxVisibility();
            }
        });

        // edit button
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StaffOrderStatusDialog dialog = new StaffOrderStatusDialog();
                dialog.show(getFragmentManager(), "update-status");
            }
        });
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


    private RestyTableCallback tablesCallback = new RestyTableCallback() {
        @Override
        public void tablesRetrieved(ArrayList<respTable> table) {

        }

        @Override
        public void tablesError(VolleyError error) {

        }
    };

    public void updateDataset(){
        fetchOrders.run();
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
            Log.e(TAG, "failure with patching: " + error.getMessage());


        }
    };
}
