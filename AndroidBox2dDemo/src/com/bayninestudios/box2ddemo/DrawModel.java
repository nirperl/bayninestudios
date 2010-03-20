package com.bayninestudios.box2ddemo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class DrawModel
{
	private FloatBuffer mVertexBuffer;
	private ShortBuffer mIndexBuffer;
	private int vertexCount = 0;

	public DrawModel(float[] coords, short[] icoords, int vertexes)
	{
		vertexCount = vertexes;
	    mVertexBuffer = makeFloatBuffer(coords);
	    mIndexBuffer = makeShortBuffer(icoords);
	}

    protected static FloatBuffer makeFloatBuffer(float[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    protected static ShortBuffer makeShortBuffer(short[] arr) {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length*4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

	public void draw(GL10 gl) {
	    gl.glFrontFace(GL10.GL_CCW);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
	    gl.glDrawElements(GL10.GL_TRIANGLE_FAN, vertexCount, GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	}

	public void draw(GL10 gl, float x, float y, float z, float rot, float scale) {
		this.draw(gl, x, y, z, rot, scale, scale);
	}

	public void draw(GL10 gl, float x, float y, float z, float rot, float scaleX, float scaleY) {
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		gl.glRotatef(rot, 0f, 0f, 1f);
		gl.glScalef(scaleX, scaleY, 1f);
		this.draw(gl);
	    gl.glPopMatrix();
	}

	public void draw(GL10 gl, float x, float y, float z, float rot) {
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		gl.glRotatef(rot, 0f, 0f, 1f);
		this.draw(gl);
	    gl.glPopMatrix();
	}

	public void draw(GL10 gl, float x, float y, float z) {
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		this.draw(gl);
	    gl.glPopMatrix();
	}
}
