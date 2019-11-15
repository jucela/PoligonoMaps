package com.inei.poligonomaps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class TouchActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "GesturesActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touch);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch (action) {
            case (MotionEvent.ACTION_DOWN):
                Log.d(DEBUG_TAG, "La accion ha sido ABAJO");
                return true;
            case (MotionEvent.ACTION_MOVE):
                Log.d(DEBUG_TAG, "La acción ha sido MOVER");
                return true;
            case (MotionEvent.ACTION_UP):
                Log.d(DEBUG_TAG, "La acción ha sido ARRIBA");
                return true;
            case (MotionEvent.ACTION_CANCEL):
                Log.d(DEBUG_TAG, "La accion ha sido CANCEL");
                return true;
            case (MotionEvent.ACTION_OUTSIDE):
                Log.d(DEBUG_TAG,"La accion ha sido fuera del elemento de la pantalla");
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }
}
