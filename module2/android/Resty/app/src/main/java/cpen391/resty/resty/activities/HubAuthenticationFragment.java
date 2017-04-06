package cpen391.resty.resty.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cpen391.resty.resty.Bluetooth.RestyBluetooth;
import cpen391.resty.resty.R;
import cpen391.resty.resty.dataStore.RestyStore;

public class HubAuthenticationFragment extends Fragment {

    private static final String TAG = "HubAuth";
    private static final Integer REQUEST_ENABLE_BT = 2;
    private RestyBluetooth restyBluetooth;
    BluetoothAdapter mBluetoothAdapter;
    private View pinText;
    private View authButton;
    private RestyStore dataStore;

    private HubAuthListener authListener;
    public interface HubAuthListener {
        void onAuth();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            authListener = (HubAuthListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement HubAuthListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dataStore = RestyStore.getInstance();

        pinText = getView().findViewById(R.id.tablePin);
        authButton = getView().findViewById(R.id.authenticate);

        // Hide authentication button until bluetooth is initialized.
        pinText.setVisibility(View.INVISIBLE);
        authButton.setVisibility(View.INVISIBLE);

        setHasOptionsMenu(false);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.hubverification, container, false);
    }

    public void initBluetooth(View view) {
        // When testing: skip Bluetooth initialization.
        if (RestyBluetooth.usingBluetooth) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                // Bluetooth not supported by this device.

                CharSequence text = "Bluetooth is not supported by this device.\n" +
                        "You will not be able to use Resty.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(getActivity(), text, duration);
                toast.show();
                return;
            } else if (!mBluetoothAdapter.isEnabled()) {
                // Ensure user enables bluetooth.

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                mBluetoothAdapter.enable();
            } else {
                try {
                    restyBluetooth = RestyBluetooth.getInstance(mBluetoothAdapter);
                    restyBluetooth.connect();
                } catch (IOException e) {
                    CharSequence text = "Please connect to Resty Hub via Bluetooth.";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getActivity(), text, duration);
                    toast.show();
                    return;
                }
            }
        }

        // Activate auth assets.
        pinText.setVisibility(View.VISIBLE);
        authButton.setVisibility(View.VISIBLE);

    }

    /** Called when user taps authenticate button */
    public void authenticate(View view) {

        // For testing purposes without bluetooth: skip straight to menu.
        if (!RestyBluetooth.usingBluetooth) {
            dataStore.put(RestyStore.Key.TABLE_ID, "0xDEFEC7EDDA7ABA5E");
            dataStore.put(RestyStore.Key.RESTAURANT_ID, "test_resto");

            authListener.onAuth();
            return;
        }

        restyBluetooth.write(((EditText) pinText).getText().toString());
        String response = restyBluetooth.listenForResponse();
        Log.d(TAG, response);

        if (response.contains("error")) {
            // Pin entry was not successful. Show error message and wait for further input.
            CharSequence text = "Incorrect Pin.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(getActivity(), text, duration);
            toast.show();

            // Clear existing user input
            ((EditText) pinText).getText().clear();
        } else {
            // Pin entry was successful. Table and Restaurant IDs have been
            // sent back, separated by whitespace.
            String[] info = response.split("\\s+");

            // Store these values in the data store.
            dataStore.put(RestyStore.Key.TABLE_ID, info[0]);
            dataStore.put(RestyStore.Key.RESTAURANT_ID, info[1]);

            authListener.onAuth();
        }
    }

}
