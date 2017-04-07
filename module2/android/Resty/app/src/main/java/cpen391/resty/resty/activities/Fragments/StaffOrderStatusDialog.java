package cpen391.resty.resty.activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import cpen391.resty.resty.Objects.Order.OrderStatus;
import java.util.Arrays;

import cpen391.resty.resty.Objects.Order;
import cpen391.resty.resty.Objects.RSOrder;
import cpen391.resty.resty.Objects.StaffUser;
import cpen391.resty.resty.Objects.User;
import cpen391.resty.resty.activities.Adapters.OrdersListViewAdapter;
import cpen391.resty.resty.serverRequests.RestyOrderPatchRequest;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyOrdersPatchCallback;

import static android.R.attr.order;

public class StaffOrderStatusDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final Object[] orders = OrdersListViewAdapter.getSelectedOrders();

        builder.setTitle("Items Selected: " + orders.length);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setItems(getNames(OrderStatus.class), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newStatus = OrderStatus.values()[i].name();
                RSOrder[] rsorders = new RSOrder[orders.length];
                for (int x = 0; x < orders.length; x++){
                    rsorders[x] = (RSOrder) orders[x];
                    rsorders[x].setStatus(newStatus);
                }
                // make request to update order status
                RestyOrderPatchRequest request = new RestyOrderPatchRequest(OrdersListViewAdapter.getOrdersPatchCallback(), getActivity());
                request.patchOrders(rsorders, StaffUser.getCurrentUser().getRestaurant_id());
            }
        });

        return builder.create();
    }

    private static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }
}
