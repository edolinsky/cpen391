package cpen391.resty.resty.activities.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cpen391.resty.resty.Objects.RSOrder;
import cpen391.resty.resty.R;
import cpen391.resty.resty.activities.Fragments.StaffOrderStatusDialog;
import cpen391.resty.resty.activities.StaffMainActivity;
import cpen391.resty.resty.serverRequests.serverCallbacks.RestyOrdersPatchCallback;

public class OrdersListViewAdapter extends ArrayAdapter<Object> implements View.OnClickListener{

    private static final int RSORDER_TYPE = 0;
    private static final int STRING_TYPE = 1;
    private Context mContext;

    public static Object[] getSelectedOrders() {
        return selectedOrders.toArray();
    }

    private static ArrayList<RSOrder> selectedOrders;
    private static RestyOrdersPatchCallback ordersPatchCallback;

    private static class ViewHolder {
        TextView txtName;
        TextView txtCustomerName;
        TextView txtStatus;
        TextView txtTable;
        CheckBox selectedCheckBox;
        int position;
    }


    private static class DividerHolder {
        TextView title;
        int position;
    }

    public OrdersListViewAdapter(ArrayList<Object> orders, Context context, ArrayList<RSOrder> selected, RestyOrdersPatchCallback callback) {
        super(context, R.layout.orders_list_item, orders);
        this.mContext = context;
        selectedOrders = selected;
        ordersPatchCallback = callback;
    }

    private boolean selectModeActivated(){
        return ((StaffMainActivity) getContext()).selectModeActivated();
    }

    @Override
    public void onClick(View v) {
        ViewHolder view = (ViewHolder) v.getTag();
        Object item = getItem(view.position);
        if (item == null) return;
        if (item.getClass() != RSOrder.class) return;

        selectedOrders.clear();
        selectedOrders.add((RSOrder) item);
        StaffOrderStatusDialog dialog = new StaffOrderStatusDialog();
        dialog.show(((Activity)mContext).getFragmentManager(), "update-status");
    }

    public static RestyOrdersPatchCallback getOrdersPatchCallback() {
        return ordersPatchCallback;
    }
    private int lastPosition = -1;

    private int getItemType(int i){
        Object item = getItem(i);
        if (item == null) return -1;
        return item.getClass() == RSOrder.class ?  RSORDER_TYPE:STRING_TYPE;
    }

    public void toggleCheckBoxVisibility(){
        ListView listView = ((StaffMainActivity)getContext()).getOrdersView();

        //listView.getVi
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view = listView.getChildAt(i);
            CheckBox box = (CheckBox) view.findViewById(R.id.RSOrderListItemSelectAllCheckBox);
            if (box != null)
                box.setVisibility(box.getVisibility() == View.VISIBLE ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent) {

        int type = getItemType(position);

        switch (type){
            case RSORDER_TYPE:
                return getRSOrderView(position, convertView, parent);
            case STRING_TYPE:
                return getStringDividerView(position, convertView, parent);
            default:
                throw new IllegalArgumentException();
        }
    }

    @NonNull
    private View getRSOrderView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        RSOrder rsorder = (RSOrder) getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null || convertView.getTag() instanceof DividerHolder) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.orders_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.OrdersListItemName);
            viewHolder.txtCustomerName = (TextView) convertView.findViewById(R.id.OrdersListCustomerName);
            viewHolder.txtStatus = (TextView) convertView.findViewById(R.id.OrdersListItemStatus);
            viewHolder.txtTable = (TextView) convertView.findViewById(R.id.OrdersItemTable);
            viewHolder.selectedCheckBox = (CheckBox) convertView.findViewById(R.id.RSOrderListItemSelectAllCheckBox);
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
        String tableText = rsorder.getOrder_id();

        viewHolder.txtName.setText(itemName);
        viewHolder.txtCustomerName.setText(customerName);
        viewHolder.txtStatus.setText(statusText);
        viewHolder.txtTable.setText(tableText);
        viewHolder.position = position;
        viewHolder.selectedCheckBox.setTag(rsorder);
        viewHolder.selectedCheckBox.setOnCheckedChangeListener(checkedChangeListener);
        viewHolder.selectedCheckBox.setVisibility(selectModeActivated() ? View.VISIBLE:View.INVISIBLE);
        return convertView;
    }

    @NonNull
    private View getStringDividerView(int position, View convertView, @NonNull ViewGroup parent) {
        // Get the data item for this position
        String title = (String) getItem(position);
        DividerHolder viewHolder;
        final View result;

        if (convertView == null || convertView.getTag() instanceof ViewHolder) {

            viewHolder = new DividerHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.rsorder_list_divider, parent, false);
            viewHolder.title = (TextView) convertView.findViewById(R.id.RSOrderDividerTitle);
            viewHolder.position = position;
            result = convertView;
            convertView.setTag(viewHolder);

        } else {

            viewHolder = (DividerHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ?
                R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (title == null) throw new NullPointerException(); // supress warning

        String titleText = "Order #" + title;
        viewHolder.title.setText(titleText);
        viewHolder.position = position;
        return convertView;
    }

    private CheckBox.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            View parent = (ViewGroup) compoundButton.getParent();
            if (parent.getTag() instanceof ViewHolder){
                if (b) {
                    selectedOrders.add((RSOrder) compoundButton.getTag());
                }else{
                    selectedOrders.remove((RSOrder) compoundButton.getTag());
                }
            }

        }
    };
}
