package cpen391.resty.resty.activities.Adapters;

/**
 * Created by annal on 2017-03-28.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cpen391.resty.resty.R;

public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final String[] web;
    private final Integer imageId;
    public CustomList(Activity context,
                      String[] web, Integer imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);

        imageView.setImageResource(imageId);
        return rowView;
    }


}