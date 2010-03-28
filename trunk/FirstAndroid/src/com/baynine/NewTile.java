package com.baynine;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class NewTile {
	private final static int VERTS = 4;

	private FloatBuffer mFVertexBuffer;
	private FloatBuffer mTexBuffer;
	private ShortBuffer mIndexBuffer;

	private int[] tileTextures = new int[1];
	private int mTextureResource;

	public NewTile(int tex) {
		mTextureResource = tex;
	    float[] coords = {
	    		0.0f, 0.0f, 0,
	    		1.0f, 0.0f, 0,
	    		0.0f, 1.0f, 0,
	    		1.0f, 1.0f, 0
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
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
                GL10.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                GL10.GL_TEXTURE_MAG_FILTER,
                GL10.GL_LINEAR);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                GL10.GL_REPEAT);
        gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                GL10.GL_REPEAT);

		Bitmap bitmap;
    	bitmap = BitmapFactory.decodeResource(mContext.getResources(), mTextureResource);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
	}

	public void draw(int x, int y, GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tileTextures[0]);
	    gl.glFrontFace(GL10.GL_CCW);
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
	    gl.glEnable(GL10.GL_TEXTURE_2D);
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
	    gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, VERTS,
	            GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	}
}
