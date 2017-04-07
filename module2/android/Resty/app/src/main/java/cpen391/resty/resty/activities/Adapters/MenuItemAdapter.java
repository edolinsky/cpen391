package cpen391.resty.resty.activities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import cpen391.resty.resty.Objects.RestaurantMenuItem;
import cpen391.resty.resty.R;

import static cpen391.resty.resty.utils.PublicConstants.MAX_ORDER;
import static cpen391.resty.resty.utils.PublicConstants.MIN_ORDER;

public class MenuItemAdapter extends ArrayAdapter<RestaurantMenuItem> implements View.OnClickListener {
    private ArrayList<RestaurantMenuItem> menu;
    Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtPrice;
        TextView txtNum;
        Button addButton;
        Button subtractButton;
        int position;
    }

    public MenuItemAdapter(ArrayList<RestaurantMenuItem> menu, Context context) {
        super(context, R.layout.menu_list_item, menu);
        this.menu = menu;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        ViewHolder line = (ViewHolder) v.getTag();
        RestaurantMenuItem item = getItem(line.position);

//        RestaurantMenuItem item = (RestaurantMenuItem) getItem((Integer)v.getTag());

        switch(v.getId()) {
            case R.id.addItem:
                if (item != null && item.getAmount() < MAX_ORDER) {
                    item.incrementAmount();
                    line.txtNum.setText(Integer.toString(item.getAmount()));
                } else {
                    // error
                }
                break;
            case R.id.subtractItem:
                if (item != null && item.getAmount() > MIN_ORDER) {
                    item.decrementAmount();
                    line.txtNum.setText(Integer.toString(item.getAmount()));
                } else {
                    // error
                }
                break;

        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        RestaurantMenuItem restaurantMenuItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.menu_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.itemCost);
            viewHolder.txtNum = (TextView) convertView.findViewById(R.id.itemAmount);
            viewHolder.addButton = (Button) convertView.findViewById(R.id.addItem);
            viewHolder.subtractButton = (Button) convertView.findViewById(R.id.subtractItem);
            viewHolder.position = position;

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ?
                R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(restaurantMenuItem.getName());
        viewHolder.txtPrice.setText(restaurantMenuItem.getPrice());
        viewHolder.txtNum.setText(String.valueOf(restaurantMenuItem.getAmount()));
        viewHolder.addButton.setOnClickListener(this);
        viewHolder.addButton.setTag(viewHolder);
        viewHolder.subtractButton.setOnClickListener(this);
        viewHolder.subtractButton.setTag(viewHolder);
        viewHolder.position = position;
        // Return the completed view to render on screen
        return convertView;
    }
}

