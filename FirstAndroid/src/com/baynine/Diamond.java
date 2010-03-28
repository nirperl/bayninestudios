package com.baynine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

//TODO: rotate and texture

public class Diamond {

	private final static int VERTS = 6;

	private FloatBuffer mFVertexBuffer;
	private ShortBuffer mIndexBuffer;
	private FloatBuffer matAmbient;
	private FloatBuffer matDiffuse;

	public Diamond() {

	    float[] matAmb = {
	    0.6f, 0.6f, 1.0f, 1.0f
	    };

	    float[] matDif = {
	    	    0.6f, 0.6f, 0.6f, 1.0f
	    	    };

	    float[] coords = {
	            // X, Y, Z
	    		0.0f,0.0f,0.5f,
	    		-0.25f,0.0f,0.0f,
	    		0.0f,-0.25f,0.0f,
	    		0.25f,0.0f,0.0f,
	    		0.0f,0.25f,0.0f,
	    		0.0f,0.0f,-0.5f
	    };

	    short[] icoords = {
	            // X, Y, Z
	    		0,1,2,
	    		0,2,3,
	    		0,3,4,
	    		0,4,1,
	    		2,1,5,
	    		3,2,5,
	    		4,3,5,
	    		1,4,5
	    };
	    mFVertexBuffer = FloatBuffer.wrap(coords);
	    mIndexBuffer = ShortBuffer.wrap(icoords);
	    matAmbient = FloatBuffer.wrap(matAmb);
	    matDiffuse = FloatBuffer.wrap(matDif);
	    mIndexBuffer.position(0);
	}

	public void loadTexture(GL10 gl, Context mContext) {
	}

	public void draw(GL10 gl) {
        gl.glDisable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_TEXTURE_2D);
//        gl.glColor4x(255, 0, 0, 1);
        gl.glColor4f(1.0f, .5f, .50f, .1f);
	    gl.glFrontFace(GL10.GL_CCW);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
	    gl.glDrawElements(GL10.GL_TRIANGLES, 24,
	            GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	}

	public void draw(GL10 gl, float x, float y, float z) {
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
		this.draw(gl);
	    gl.glPopMatrix();
	}
}
