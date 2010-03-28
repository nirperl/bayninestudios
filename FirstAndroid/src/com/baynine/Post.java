package com.baynine;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

public class Post {
	private int[] tileTextures = new int[1];
	private int mTextureResource;
	private int FACES = 6;
	private FloatBuffer mVertexBuffer;
	private FloatBuffer mTexBuffer;
	private ShortBuffer mIndexBuffer;
	private float width = 0.15f;
	private float dynamic = -0.15f;
	private float[] coords = {
            // X, Y, Z
    		-width,0.0f,-0.01f,
    		0.0f,-width,-0.01f,
    		-width,0.0f,1.0f,
    		0.0f,-width,1.0f,
    		width,0.0f,-0.01f,
    		width,0.0f,1.0f,
    		// cap
    		-width,0.0f,1.0f,
    		0.0f,-width,1.0f,
    		width,0.0f,1.0f,
    		0.0f,width,1.0f
    	};

	public Post(int tex) {
		mTextureResource = tex;
	    float[] tcoords = {
	            // X, Y, Z
	    		0.0f, 1.0f,
	    		0.5f, 1.0f,
	    		0.0f, 0.0f,
	    		0.5f, 0.0f,
	    		1.0f, 1.0f,
	    		1.0f, 0.0f,

	    		0.0f, .15f,
	    		.15f, .15f,
	    		.15f, 0.0f,
	    		0.0f, 0.0f
	    };
	    
	    short[] icoords = {
	            // X, Y, Z
	    		0,1,2,
	    		1,3,2,
	    		1,4,3,
	    		4,5,3,
	    		6,7,9,
	    		7,8,9
	    };

	    mVertexBuffer = FloatBuffer.wrap(coords);
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

	public void draw(GL10 gl) {
	    gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, tileTextures[0]);
	    gl.glFrontFace(GL10.GL_CCW);
	    mVertexBuffer.put(0, dynamic);
//	    dynamic = dynamic - 0.001f;
	    if (dynamic < -.3f)
	    {
	    	dynamic = -.1f;
	    }
	    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
	    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
//	    gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
	    gl.glDrawElements(GL10.GL_TRIANGLES, FACES * 3,
	            GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
	    gl.glDisable(GL10.GL_TEXTURE_2D);
	}

	public void draw(GL10 gl, float x, float y, float z, float rot) {
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);
		gl.glRotatef(rot, 0f, 0f, 1f);
		this.draw(gl);
	    gl.glPopMatrix();
	}

	public void draw(GL10 gl, float x, float y, float z) {
		gl.glPushMatrix();
		gl.glTranslatef(x, y, 0);
		this.draw(gl);
	    gl.glPopMatrix();
	}
}
