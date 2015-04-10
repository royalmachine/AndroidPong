package com.example.teodor.androidpong;
import android.os.Message;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.neurosky.thinkgear.TGDevice;

/**
 * Created by TEODOR on 03-Mar-15.
 */
public class GameState {

    //screen width and height
    final int _screenWidth = 1060;
    final int _screenHeight = 1800;

    //The ball
    final int _ballSize = 50;
    int _ballX = 100;
    int _ballY = 100;
    int _ballVelocityX = 3;
    int _ballVelocityY = 3;
//
    //The bats
    final int _batLength = 300;
    final int topBathLength = 3000;
    final int _batHeight = 40;
    int _topBatX = 0;
    final int _topBatY = 20;
    int _bottomBatX = (_screenWidth/2) - (_batLength / 2);
    final int _bottomBatY = 1400;
    final int _batSpeed = 3;

    public GameState()
    {
    }

    //The update method
    public void update() {

        _ballX += _ballVelocityX;
        _ballY += _ballVelocityY;

//DEATH!
        if(_ballY > _screenHeight || _ballY < 0)
        {_ballX = 100; 	_ballY = 100;}  	//Collisions with the sides

        if(_ballX > _screenWidth || _ballX < 0)
            _ballVelocityX *= -1; 	//Collisions with the bats

        if(_ballX > _topBatX && _ballX < _topBatX+topBathLength && _ballY < _topBatY)
            _ballVelocityY *= -1;  //Collisions with the bats

        if(_ballX > _bottomBatX && _ballX < _bottomBatX+_batLength
                && _ballY > _bottomBatY)
            _ballVelocityY *= -1;
    }



    public boolean surfaceTouched(float posX, float posY) {
        _bottomBatX = (int) posX;

        return true;
    }

    public void brainActivity(Message msg)
    {
        Log.v("HelloEEG", msg.arg1 + "Value of thought");
        if(msg.what == TGDevice.MSG_BLINK) //left
        {
            //if(msg.arg1 > 40)

            _bottomBatX -= _batSpeed * 25;
        }

        else
        if(msg.what ==TGDevice.MSG_ATTENTION) //right
        {
            if(msg.arg1 > 45)

                _bottomBatX += _batSpeed * (msg.arg1 - 35);

        }

        //return true;
    }

    //the draw method
    public void draw(Canvas canvas, Paint paint) {

//Clear the screen
        canvas.drawRGB(20, 20, 20);

//set the colour
        paint.setARGB(200, 0, 200, 0);

//draw the ball
        canvas.drawRect(new Rect(_ballX,_ballY,_ballX + _ballSize,_ballY + _ballSize),
                paint);

//draw the bats
        canvas.drawRect(new Rect(_topBatX, _topBatY, _topBatX + topBathLength,
                _topBatY + _batHeight), paint); //top bat
        canvas.drawRect(new Rect(_bottomBatX, _bottomBatY, _bottomBatX + _batLength,
                _bottomBatY + _batHeight), paint); //bottom bat

    }
}