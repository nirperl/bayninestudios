package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

// just a placeholder class to draw walls with.  Really should be in the map definition
public class Cave
{
    DrawModel model;
    public boolean inside = false;
    
    public Cave(Context context)
    {
        model = new DrawModel(context, R.xml.wall);
    }

    public void loadTextures(GL10 gl, Context context)
    {
        model.loadTexture(gl, context, R.drawable.cavewall);
    }
    
    public void draw(GL10 gl)
    {
        if (inside)
        {
            model.draw(gl, 48f, 22f, 0f);
            model.draw(gl, 49f, 22f, 0f);
            model.draw(gl, 50f, 22f, 0f);
            model.draw(gl, 51f, 22f, 0f);
            model.draw(gl, 52f, 22f, 0f);
            model.draw(gl, 53f, 22f, 0f);
    
            model.draw(gl, 51f, 21f, 0f);
            model.draw(gl, 52f, 21f, 0f);
            model.draw(gl, 53f, 21f, 0f);
    
            model.draw(gl, 48f, 22f, 0f, -90f);
            model.draw(gl, 48f, 20f, 0f, -90f);
            model.draw(gl, 48f, 21f, 0f, -90f);
            model.draw(gl, 51f, 19f, 0f, 90f);
            model.draw(gl, 51f, 20f, 0f, 90f);
    //        model.draw(gl, 51f, 21f, 0f, 90f);
    
            model.draw(gl, 49f, 19f, 0f, 180f);
            model.draw(gl, 51f, 19f, 0f, 180f);
    
            model.draw(gl, 49f, 19f, 0f, -90f);
            model.draw(gl, 50f, 18f, 0f, 90f);
            model.draw(gl, 49f, 18f, 0f, -90f);
            model.draw(gl, 50f, 17f, 0f, 90f);
            model.draw(gl, 49f, 17f, 0f, -90f);
            model.draw(gl, 50f, 16f, 0f, 90f);
        }
        else
        {
            model.draw(gl, 47f, 15f, 0f, 180f);
            model.draw(gl, 48f, 15f, 0f, 180f);
            model.draw(gl, 49f, 15f, 0f, 180f);
            model.draw(gl, 51f, 15f, 0f, 180f);
            model.draw(gl, 52f, 15f, 0f, 180f);
            model.draw(gl, 53f, 15f, 0f, 180f);

            model.draw(gl, 47f, 15f, 1f, 180f);
            model.draw(gl, 48f, 15f, 1f, 180f);
            model.draw(gl, 49f, 15f, 1f, 180f);
            model.draw(gl, 50f, 15f, 1f, 180f);
            model.draw(gl, 51f, 15f, 1f, 180f);
            model.draw(gl, 52f, 15f, 1f, 180f);
            model.draw(gl, 53f, 15f, 1f, 180f);

            model.draw(gl, 49f, 15f, 2.0f, 180f);
            model.draw(gl, 50f, 15f, 2.0f, 180f);
            model.draw(gl, 51f, 15f, 2.0f, 180f);
        }
        model.draw(gl, 49f, 16f, 0f, -90f);
        model.draw(gl, 50f, 15f, 0f, 90f);
    }
}
