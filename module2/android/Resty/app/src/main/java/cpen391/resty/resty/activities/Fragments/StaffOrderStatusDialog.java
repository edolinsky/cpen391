package cpen391.resty.resty.activities.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

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

public class StaffOrderStatusDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedStateInstance){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final RSOrder order = OrdersListViewAdapter.getSelectedOrder();

        builder.setTitle(order.getName());
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
                RSOrder[] orders = new RSOrder[1];
                orders[0] = order;
                // make request to update order status
                RestyOrderPatchRequest request = new RestyOrderPatchRequest(callback);
                request.patchOrders(orders, StaffUser.getCurrentUser().getRestaurant_id());
            }
        });

        return builder.show();
    }

    final RestyOrdersPatchCallback callback = new RestyOrdersPatchCallback() {
        @Override
        public void ordersUpdateSuccess() {
            Log.i("Patch Success", "successfull patching dude");
        }

        @Override
        public void ordersUpdateError(VolleyError error) {
            Log.e("Patch Fail", "failure with patching dude: " + error.getMessage());

        }
    };

    private static String[] getNames(Class<? extends Enum<?>> e) {
        return Arrays.toString(e.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
    }
}
