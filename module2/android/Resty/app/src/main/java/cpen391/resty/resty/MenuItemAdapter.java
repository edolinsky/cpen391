package cpen391.resty.resty;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MenuItemAdapter extends ArrayAdapter<MenuItem> implements View.OnClickListener {
    private ArrayList<MenuItem> menu;
    Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtPrice;
        EditText editNum;
    }

    public MenuItemAdapter(ArrayList<MenuItem> menu, Context context) {
        super(context, R.layout.menu_list_item, menu);
        this.menu = menu;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        // get description of item
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        MenuItem menuItem = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.menu_list_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.itemCost);
            viewHolder.editNum = (EditText) convertView.findViewById(R.id.itemAmount);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ?
                R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(menuItem.getName());
        viewHolder.txtPrice.setText(menuItem.getPrice());
        viewHolder.editNum.setText(String.valueOf(menuItem.getAmount()));
        // Return the completed view to render on screen
        return convertView;
    }
}

