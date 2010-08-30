package com.bayninestudios.eldania;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.os.Bundle;

public class DialogActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TextView tv = new TextView(this);
        tv.setText("Hello, Android");
        setContentView(tv);
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            finish();
        }
        // Sleep so that the main thread doesn't get flooded with UI events.
        try {
            Thread.sleep(32);
        } catch (InterruptedException e) {
            // No big deal if this sleep is interrupted.
        }
        return true;
    }

    @Override
    public boolean onKeyUp(final int keyCode, KeyEvent msg)
    {
        if (keyCode == KeyEvent.KEYCODE_2)
        {
            this.finish();
        }
        return true;
    }
}
