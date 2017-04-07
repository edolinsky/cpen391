package cpen391.resty.resty.activities.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.util.List;

import cpen391.resty.resty.Objects.RestaurantMenuItem;
import cpen391.resty.resty.R;

public class OrderDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        MenuFragment parent;
        try {
            parent = (MenuFragment) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(getTargetFragment().toString()
                    + " must be MenuFragment");
        }

        builder.setTitle(R.string.confirmOrder)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_OK, null);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getTargetFragment().onActivityResult(getTargetRequestCode(),
                                Activity.RESULT_CANCELED, null);
                    }
                });

        builder.setItems(createItemsForDialog(parent.getOrder()), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

    private String[] createItemsForDialog(List<RestaurantMenuItem> items) {
        String[] displayItems = new String[items.size()];

        for(int i = 0; i < items.size(); i++) {
            displayItems[i] = items.get(i).getAmount() + " x " + items.get(i).getName();
        }

        return displayItems;
    }
}
