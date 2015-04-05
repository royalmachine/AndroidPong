package com.example.teodor.androidpong;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.neurosky.thinkgear.*;
import android.bluetooth.BluetoothAdapter;

/**
 * Created by TEODOR on 03-Mar-15.
 */
class GameView extends SurfaceView  implements SurfaceHolder.Callback {
    private GameThread _thread;
    private float _x = 0;
    private float _y = 0;
    private MainActivity my;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //So we can listen for events...
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);

        //and instantiate the thread
        _thread = new GameThread(holder, context, new Handler());
    }


   public boolean brainStuff (Message msg) {
       my.handler.handleMessage(msg);
     _thread.getGameState().brainActivity(msg);
       return true;
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