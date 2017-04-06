package cpen391.resty.resty.Bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Set;

public class RestyBluetooth {

    private static final String TAG = "RestyBluetooth";
    private static final String RESTY_BLUETOOTH_TAG = "RESTY";
    private static final String MESSAGE_START = "````````````````````";
    private static final String MESSAGE_END = "`";
    private static final char SPECIAL_CHAR = '`';

    public static final boolean usingBluetooth = false;

    private OutputStream outputStream;
    private InputStream inputStream;
    private BluetoothSocket btSocket;
    private BluetoothDevice device;
    public BluetoothAdapter adapter;

    private static RestyBluetooth restyBluetooth;

    private RestyBluetooth(BluetoothAdapter bluetoothAdapter) throws IOException {
        adapter = bluetoothAdapter;

        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice btDevice : pairedDevices) {

                // Choose the first device name with
                if (btDevice.getName().contains(RESTY_BLUETOOTH_TAG)) {
                    device = btDevice;
                    break;
                }
            }
        }

        if (pairedDevices.size() <= 0 || device == null) {
            throw new IOException();
        }
    }

    public static RestyBluetooth getInstance(BluetoothAdapter bluetoothAdapter) throws IOException {
        if (restyBluetooth == null) {
            restyBluetooth = new RestyBluetooth(bluetoothAdapter);
        }

        return restyBluetooth;
    }

    public static RestyBluetooth getInstance() {
        if (restyBluetooth != null) {
            return restyBluetooth;
        }

        throw new IllegalArgumentException("Should use getInstance(BluetoothAdapter) at least once before using this method.");
    }

    /**
     * Connect to the paired device's bluetooth socket.
     */
    public void connect() {

        // connect to bluetooth device.
        try {
            ParcelUuid[] uuids = device.getUuids();
            btSocket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
            btSocket.connect();
            outputStream = btSocket.getOutputStream();
            inputStream = btSocket.getInputStream();
        } catch (IOException e) {
            Log.e(TAG, "IO exception:\n" + e.toString());
        }
    }

    /**
     * Disconnect from the peired device's bluetooth socket.
     */
    public void disconnect() {
        try {
            outputStream.close();
            inputStream.close();
            btSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "IO exception:\n" + e.toString());
        }
    }

    /**
     * Write the specified message over a bluetooth socket. Requires a previous call to connect().
     * @param message string message to be sent via bluetooth.
     */
    public void write(String message) {
        try {
            outputStream.write((MESSAGE_START + message + MESSAGE_END).getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            Log.e(TAG, "IO exception\n:" + e.toString());
        }
    }

    /**
     * Listen for a message over bluetooth. Requires connect() be called previously. Blocking.
     * @return message string sent to this device.
     */
    public String listenForResponse() {
        final int BUFFER_SIZE = 128;
        byte[] buffer = new byte[BUFFER_SIZE];
        int i = 0;

        try {

            // Skip over all leading non-special characters.
            while (inputStream.read() != SPECIAL_CHAR) {
                i = 0;
            }

            // Skip over all leading special characters.
            int x;
            do {
                x = inputStream.read();
            } while (x == SPECIAL_CHAR);
            buffer[i] = (byte) x;
            i++;

            // Read in message, break at final special character.
            while (i < BUFFER_SIZE) {
                x = inputStream.read();
                if (x == SPECIAL_CHAR) {
                    break;
                }
                buffer[i++] = (byte) x;
            }

        } catch (IOException e) {
            Log.e(TAG, "IO exception:\n" + e.toString());
        }

        byte[] subBuffer = Arrays.copyOfRange(buffer, 0, i);
        return new String(subBuffer);
    }
}
