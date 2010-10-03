package com.bayninestudios.eldania;

import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class RippleEffect
{
    private Particle[] ripple;
    private long rippleUpdate;
    private int RIPPLES = 20;
    private float ripple_x = 50.4f;
    private float ripple_y = 13.4f;
    private DrawModel targetTile;

    public RippleEffect(Context context)
    {
        targetTile = new DrawModel(context, R.xml.ripple);
        ripple = new Particle[RIPPLES];
        for (int loop = 0; loop < RIPPLES; loop++)
        {
            ripple[loop] = new Particle(Util.randomFloat()*.2f + ripple_x,
                    Util.randomFloat()*.2f + ripple_y, 0.01f);
            ripple[loop].scale = Util.randomFloat()*.08f+.02f;
            rippleUpdate = System.currentTimeMillis();
        }
    }

    public void init(GL10 gl, Context context)
    {
        targetTile.loadTexture(gl, context, R.drawable.target);
    }

    public void update()
    {
        long newTime = System.currentTimeMillis();
        newTime = newTime - rippleUpdate;
        float frameRate = newTime / 1000f;
        float scaleFactor = 0.00001f * frameRate;
        Log.d("DDDDDD ",":"+newTime);
        for (int loop = 0; loop < RIPPLES; loop++)
        {
            ripple[loop].scale = ripple[loop].scale + 0.005f;
            if (ripple[loop].scale > 0.1f)
            {
                ripple[loop].x = Util.randomFloat()*.2f+ripple_x;
                ripple[loop].y = Util.randomFloat()*.2f+ripple_y;
                ripple[loop].scale = 0.0001f;
            }
        }
        rippleUpdate = newTime;
    }
    
    public void draw(GL10 gl)
    {
        // Ripples!!!!
        gl.glEnable(GL10.GL_BLEND);
        for (int loop = 0; loop < RIPPLES; loop++)
        {
            targetTile.draw(gl, ripple[loop].x, ripple[loop].y, ripple[loop].z, 0f, ripple[loop].scale);
        }
        gl.glDisable(GL10.GL_BLEND);
        Log.d("debug", "wtf");
    }
}
