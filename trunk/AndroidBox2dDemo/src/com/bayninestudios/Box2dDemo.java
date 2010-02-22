package com.bayninestudios;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class Box2dDemo extends Activity {

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(new DrawView(this));
    }
}