package cpen391.resty.resty.activities.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import junit.framework.Assert;

import java.util.ArrayList;

import cpen391.resty.resty.Objects.RSOrder;
import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.Fragments.StaffOrderStatusDialog;

public class OrdersListViewAdapter extends ArrayAdapter<RSOrder> implements View.OnClickListener{

    private Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtCustomerName;
        TextView txtStatus;
        TextView txtTable;
        int position;
    }

    public OrdersListViewAdapter(ArrayList<RSOrder> orders, Context context) {
        super(context, R.layout.orders_list_item, orders);
        this.addAll(orders);
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        ViewHolder view = (ViewHolder) v.getTag();
        RSOrder item = getItem(view.position);
        if (item == null) return;

        selectedOrder = item;
        StaffOrderStatusDialog dialog = new StaffOrderStatusDialog();
        dialog.show(((Activity)mContext).getFragmentManager(), "update-status");
    }

    public static RSOrder getSelectedOrder() {
        return selectedOrder;
    }

    private static RSOrder selectedOrder = null;
    private int lastPosition = -1;

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Get the data item for this position
        RSOrder rsorder = getItem(position);
        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.orders_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.OrdersListItemName);
            viewHolder.txtCustomerName = (TextView) convertView.findViewById(R.id.OrdersListCustomerName);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.OrdersListItemStatus);
            viewHolder.txtTable = (TextView) convertView.findViewById(R.id.OrdersItemTable);
            viewHolder.position = position;
            result = convertView;
            convertView.setOnClickListener(this);
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ?
                R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (rsorder == null) throw new NullPointerException(); // supress warning

        String itemName = rsorder.getName();
        String customerName = "Customer: " + rsorder.getCustomer_name();
        String statusText = "Status: " + rsorder.getStatus();
        String tableText = rsorder.getTable_id();

        viewHolder.txtName.setText(itemName);
        viewHolder.txtCustomerName.setText(customerName);
        viewHolder.txtStatus.setText(statusText);
        viewHolder.txtTable.setText(tableText);
        viewHolder.position = position;

        return convertView;
    }

}
