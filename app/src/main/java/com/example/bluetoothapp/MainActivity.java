package com.example.bluetoothapp;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private OutputStream outputStream;
    private InputStream inStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendPacket(View view) throws IOException {
        // Do something in response to crawl button
        //update gui w/ suspicion level
        System.out.println("Initializing");
        init();
        System.out.println("Initialized");
        String[] packets = {"p1", "p2", "p3"}; // REPLACE WITH ACTUAL PACKETS (converts string to byte array)
        for(int i = 0; i < packets.length; i++)
        {
            write(packets[i]);
            TextView num = (TextView) findViewById(R.id.str2);
            String display = "Number of packets sent: " + i;
            num.setText(display);
        }
    }

    private void init() throws IOException {
        // initialize
        BluetoothAdapter blueAdapter = BluetoothAdapter.getDefaultAdapter();
        if (blueAdapter != null) {
            if (blueAdapter.isEnabled()) {
                Set<BluetoothDevice> bondedDevices = blueAdapter.getBondedDevices();

                if(bondedDevices.size() > 0) {
                    Object[] devices = (Object []) bondedDevices.toArray();
                    BluetoothDevice device = (BluetoothDevice) devices[0];
                    ParcelUuid[] uuids = device.getUuids();
                    BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuids[0].getUuid());
                    socket.connect();
                    outputStream = socket.getOutputStream();
                    inStream = socket.getInputStream();
                }

                Log.e("error", "No appropriate paired devices.");
            } else {
                Log.e("error", "Bluetooth is disabled.");
            }
        }
    }

    public void write(String s) throws IOException {
        outputStream.write(s.getBytes());
    }
}
