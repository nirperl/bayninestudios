package com.bayninestudios.eldania;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class HealthBar
{
    private final static int VERTS = 4;

    private FloatBuffer mFVertexBuffer;
    private ShortBuffer mIndexBuffer;

    public HealthBar()
    {
        float[] coords = { 0.0f, 0.0f, 0, 0.1f, 0.0f, 0, 0.1f, 0.1f, 0, 0.0f, 0.1f, 0 };
        short[] icoords = { 0, 1, 2, 3 };

        mFVertexBuffer = Util.makeFloatBuffer(coords);
        mIndexBuffer = Util.makeShortBuffer(icoords);
    }

    public void draw(GL10 gl, float scalex)
    {
        gl.glDisable(GL10.GL_TEXTURE_2D);
        gl.glPushMatrix();
        gl.glTranslatef(-4.7f, 2.6f, 0.0f);
        gl.glScalef(scalex, 1f, 1f);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_FAN, VERTS, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        gl.glPopMatrix();
    }

}
