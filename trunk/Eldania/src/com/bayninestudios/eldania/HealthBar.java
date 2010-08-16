package com.bayninestudios.eldania;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
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
	    float[] coords = {
	    		0.0f, 0.0f, 0,
	    		0.1f, 0.0f, 0,
	    		0.1f, 0.1f, 0,
	    		0.0f, 0.1f, 0
	    };
	
	    short[] icoords = {0,1,2,3};

	    mFVertexBuffer = makeFloatBuffer(coords);
	    mIndexBuffer = makeShortBuffer(icoords);
	}

	// used to make native order float buffers
	private FloatBuffer makeFloatBuffer(float[] arr)
	{
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

	// used to make native order short buffers
    private ShortBuffer makeShortBuffer(short[] arr)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    public void draw(GL10 gl, float scalex)
    {
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glPushMatrix();
//        gl.glColor4f(1f, 0f, 0f, 1f);
		gl.glTranslatef(-4.7f, 2.6f, 0.0f);
		gl.glScalef(scalex, 1f, 1f);
	    gl.glFrontFace(GL10.GL_CCW);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
	    gl.glDrawElements(GL10.GL_TRIANGLE_FAN, VERTS,
	            GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
		gl.glPopMatrix();
	}

}
