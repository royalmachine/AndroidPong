package com.example.teodor.androidpong;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;


public class MainActivity extends ActionBarActivity {

    BluetoothAdapter bluetoothAdapter;
    TGDevice tgDevice;
    final boolean rawEnabled = false;
    GameView view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        tgDevice = new TGDevice(bluetoothAdapter, handler);
        Toast.makeText(this,"bullshit",Toast.LENGTH_SHORT).show();
    }

    public void onDestroy() {
        tgDevice.close();
        super.onDestroy();
    }

    private final Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            view.brainStuff(msg);

        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void doStuff(View view) {
        if(tgDevice.getState() != TGDevice.STATE_CONNECTING && tgDevice.getState() != TGDevice.STATE_CONNECTED)
            tgDevice.connect(rawEnabled);
        //tgDevice.ena
    }
}
