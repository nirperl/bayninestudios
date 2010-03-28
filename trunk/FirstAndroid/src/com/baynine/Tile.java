package com.baynine;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Tile
{
	private final static int VERTS = 4;

	private FloatBuffer mFVertexBuffer;
	private FloatBuffer mTexBuffer;
	private ShortBuffer mIndexBuffer;

	private int[] tileTextures = new int[1];
	private int mTextureResource;

	public Tile(int tex)
	{
		mTextureResource = tex;
	    float[] coords = {
	    		0.0f,0.0f,0.0f,
	    		1.0f,0.0f,0.0f,
	    		0.0f,1.0f,0.0f,
	    		1.0f,1.0f,0.0f
	    };
	
	    float[] tcoords = {
	    		0.0f, 1.0f,
	    		1.0f, 1.0f,
	    		0.0f, 0.0f,
	    		1.0f, 0.0f
	    };
	    short[] icoords = {0,1,2,3};

	    mFVertexBuffer = FloatBuffer.wrap(coords);
	    mTexBuffer = FloatBuffer.wrap(tcoords);
	    mIndexBuffer = ShortBuffer.wrap(icoords);
	}

	public void loadTexture(GL10 gl, Context mContext) {
        gl.glGenTextures(1, tileTextures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tileTextures[0]);
		Bitmap bitmap;
    	bitmap = BitmapFactory.decodeResource(mContext.getResources(), mTextureResource);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
	}

	public void bindTex(GL10 gl)
	{
	    gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tileTextures[0]);
	}

	public void draw(int x, int y, GL10 gl) {
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
	    gl.glPushMatrix();
	    gl.glTranslatef((float)x, (float)y, 0.0f);
	    gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, VERTS,
	            GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	    gl.glPopMatrix();
	}
}
