package com.bayninestudios.eldania;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Speedo
{
    private final static int VERTS = 3;
    private final static int FRAMECOUNT = 200;

    private FloatBuffer mFVertexBuffer;
    private ShortBuffer mIndexBuffer;

    private long startTime;
    private long endTime;

    private float mFrameRate;
    private long[] lastTenFrames;
    private int currentFrame = 0;

    public Speedo()
    {
        lastTenFrames = new long[FRAMECOUNT];
        float[] coords = { -0.05f, 0.0f, 0, 0.05f, 0.0f, 0, 0.0f, .40f, 0 };

        short[] icoords = { 0, 1, 2 };

        mFVertexBuffer = Util.makeFloatBuffer(coords);
        mIndexBuffer = Util.makeShortBuffer(icoords);
    }

    public void draw(GL10 gl)
    {
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glPushMatrix();
        gl.glColor4f(1f, 1f, 1f, 1f);
        gl.glTranslatef(4.5f, 2.3f, 0.0f);
        gl.glRotatef((-1.5f * mFrameRate) + 90f, 0f, 0f, 1f);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLES, VERTS, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        gl.glPopMatrix();
    }

    private void recordRate(long mseconds)
    {
        float totalRate = 0.0f;
        lastTenFrames[currentFrame] = mseconds;
        currentFrame++;
        if (currentFrame == FRAMECOUNT)
        {
            Log.d("Framerate", Float.toString(mFrameRate));
            currentFrame = 0;
            for (int i = 0; i < FRAMECOUNT; i++)
            {
                totalRate = totalRate + lastTenFrames[i];
            }
            mFrameRate = 1000f / (totalRate / FRAMECOUNT);
        }
    }

    public void setStartTime()
    {
        startTime = System.currentTimeMillis();
    }

    public void setEndTime()
    {
        endTime = System.currentTimeMillis();
        recordRate(endTime - startTime);
    }

    public float getRate()
    {
        return mFrameRate;
    }

}
