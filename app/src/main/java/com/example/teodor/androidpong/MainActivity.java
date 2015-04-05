package com.example.teodor.androidpong;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.neurosky.thinkgear.TGDevice;
import com.neurosky.thinkgear.TGEegPower;


public class MainActivity extends ActionBarActivity {



    BluetoothAdapter btAdapter;
    TGDevice tgDevice;
    final boolean rawEnabled = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.v("BT:", btAdapter.getName());
        if (btAdapter != null) {
            tgDevice = new TGDevice(btAdapter, handler);
            tgDevice.connect(true);
            Log.v("Device", "" + tgDevice.getState());

        }
    }

    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TGDevice.MSG_STATE_CHANGE:
                    Log.v("HelloEEG", "State changed");
                    switch (msg.arg1) {
                        case TGDevice.STATE_IDLE:
                            break;
                        case TGDevice.STATE_CONNECTING:
                            Log.v("HelloEEG", "Connecting");
                            Toast connecting = Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_SHORT);
                            connecting.show();
                            break;
                        case TGDevice.STATE_CONNECTED:
                            tgDevice.start();
                            Log.v("HelloEEG", "Connected");
                            Toast connected = Toast.makeText(getApplicationContext(), "Connected to " + tgDevice.getConnectedDevice().getName(), Toast.LENGTH_SHORT);
                            connected.show();
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            Toast disconnected = Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT);
                            disconnected.show();
                            break;
                        case TGDevice.STATE_NOT_FOUND:
                        case TGDevice.STATE_NOT_PAIRED:
                        default:
                            break;
                    }
                    break;
                case TGDevice.MSG_POOR_SIGNAL:
// Log.v("HelloEEG", "PoorSignal: " + msg.arg1);
                case TGDevice.MSG_ATTENTION:
                    Log.v("HelloEEG", "Attention: " + msg.arg1);
                    break;
                case TGDevice.MSG_BLINK:
//Log.v("HelloEEG", "Blinks:" + msg.arg1);
                    break;
                case TGDevice.MSG_MEDITATION:
                    Log.v("HelloEEG", "Meditation:" + msg.arg1);
                    //rj2.setMotorSpeed(msg.arg1);
// progressMeditation.setProgress(msg.arg1);
                    break;
                case TGDevice.MSG_RAW_DATA:
// int rawValue = msg.arg1;
// Log.v("HelloEEG", "Raw Data: "+ rawValue);
                    break;
                case TGDevice.MSG_EEG_POWER:
                    TGEegPower ep = (TGEegPower) msg.obj;
//Log.v("HelloEEG", "Delta: " + ep.delta); default:
                    break;
            }
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
