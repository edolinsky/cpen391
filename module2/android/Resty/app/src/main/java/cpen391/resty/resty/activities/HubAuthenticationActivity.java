package cpen391.resty.resty.activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import cpen391.resty.resty.Bluetooth.RestyBluetooth;
import cpen391.resty.resty.R;
import cpen391.resty.resty.dataStore.RestyStore;

public class HubAuthenticationActivity extends MainActivityBase {

    private static final String TAG = "HubAuth";
    private static final Integer REQUEST_ENABLE_BT = 2;
    private RestyBluetooth restyBluetooth;
    BluetoothAdapter mBluetoothAdapter;
    private EditText pinText;
    private RestyStore dataStore;

    boolean testing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataStore = RestyStore.getInstance();

        // When testing: skip Bluetooth initialization.
        if (testing) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            if (mBluetoothAdapter == null) {
                // Bluetooth not supported by this device.

                CharSequence text = "Bluetooth is not supported by this device.\n" +
                        "You will not be able to use Resty.";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(this, text, duration);
                toast.show();
            } else if (!mBluetoothAdapter.isEnabled()) {
                // Ensure user enables bluetooth.

                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            } else {
                try {
                    restyBluetooth = new RestyBluetooth(mBluetoothAdapter);
                    restyBluetooth.connect();
                } catch (IOException e) {
                    CharSequence text = "Please connect to Resty Hub via Bluetooth.";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(this, text, duration);
                    toast.show();
                }
            }
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hubverification);

        pinText = (EditText) findViewById(R.id.tablePin);

    }

    /** Called when user taps authenticate button */
    public void authenticate(View view) {

        // For testing purposes without bluetooth: skip straight to menu.
        if (testing) {
            dataStore.put(RestyStore.Key.TABLE_ID, "0xDEFEC7EDDA7ABA5E");
            dataStore.put(RestyStore.Key.RESTAURANT_ID, "test_resto");

            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
            return;
        }

        restyBluetooth.write(pinText.getText().toString());
        String response = restyBluetooth.listenForResponse();
        Log.d(TAG, response);

        if (response.contains("error")) {
            CharSequence text = "Incorrect Pin.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(this, text, duration);
            toast.show();
        } else {
            // Pin entry was successful. Table and Restaurant IDs have been
            // sent back, separated by whitespace.
            String[] info = response.split("\\s+");

            // Store these values in the data store.
            dataStore.put(RestyStore.Key.TABLE_ID, info[0]);
            dataStore.put(RestyStore.Key.RESTAURANT_ID, info[1]);

            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
    }

}
