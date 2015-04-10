package com.example.teodor.androidpong;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.neurosky.thinkgear.*;
import android.bluetooth.BluetoothAdapter;
import android.widget.Toast;

/**
 * Created by TEODOR on 03-Mar-15.
 */
class GameView extends SurfaceView  implements SurfaceHolder.Callback {
    private GameThread _thread;
    private float _x = 0;
    private float _y = 0;
    public TGDevice tgDevice;
    Context context;
    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //So we can listen for events...
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        this.context= context;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.v("BT:", btAdapter.getName());
        if (btAdapter != null) {
            tgDevice = new TGDevice(btAdapter, handler);
            tgDevice.connect(true);
            Log.v("Device", "" + tgDevice.getState());

            //and instantiate the thread
            _thread = new GameThread(holder, context, new Handler());
        }
    }



    public final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //_thread.getGameState().brainActivity(msg);
            switch (msg.what) {
                case TGDevice.MSG_STATE_CHANGE:
                    Log.v("HelloEEG", "State changed");
                    switch (msg.arg1) {
                        case TGDevice.STATE_IDLE:
                            break;
                        case TGDevice.STATE_CONNECTING:
                            Log.v("HelloEEG", "Connecting");
                            Toast connecting = Toast.makeText(context, "Connecting", Toast.LENGTH_SHORT);
                            connecting.show();
                            break;
                        case TGDevice.STATE_CONNECTED:
                            tgDevice.start();
                            Log.v("HelloEEG", "Connected");
                            Toast connected = Toast.makeText(context, "Connected to " + tgDevice.getConnectedDevice().getName(), Toast.LENGTH_SHORT);
                            connected.show();
                            break;
                        case TGDevice.STATE_DISCONNECTED:
                            Toast disconnected = Toast.makeText(context, "Disconnected", Toast.LENGTH_SHORT);
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
                    _thread.getGameState().brainActivity(msg);
                    Log.v("HelloEEG", "Attention: " + msg.arg1);
                    break;
                case TGDevice.MSG_BLINK:
                    _thread.getGameState().brainActivity(msg);
 Log.v("HelloEEG", "Blinks:" + msg.arg1);
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


    public void brainstuff(Message msg)
    {
        _thread.getGameState().brainActivity(msg);
    }
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            _x = event.getX();
            _y = event.getY();
            _thread.getGameState().surfaceTouched(_x, _y);
        }
        return true;
    }

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        //Mandatory, just swallowing it for this example

    }

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        _thread.start();
    }

    //Implemented as part of the SurfaceHolder.Callback interface
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        _thread.stop();
    }
}