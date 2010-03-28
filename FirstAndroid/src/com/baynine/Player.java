package com.baynine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class Player {

    private final static int VERTS = 4;


    private FloatBuffer mFVertexBuffer;
    private FloatBuffer mTexBuffer;
    private ShortBuffer mIndexBuffer;
	private int[] playerTextures = new int[1];
	private int mTextureResource;

    public Player(int tex) {
    	mTextureResource = tex;
        float[] coords = {
                // X, Y, Z
        		-0.5f, 0.0f, 0.0f,
        		0.5f, 0.0f, 0.0f,
        		-0.5f, 0.0f, 2.0f,
        		0.5f, 0.0f, 2.0f
        };

        float[] tcoords = {
                // X, Y, Z
        		0.0f, 1.0f,
        		.95f, 1.0f,
        		0.0f, 0.01f,
        		.950f, 0.01f
        };

	    short[] icoords = {
	            // X, Y, Z
	    		0,1,2,3
	    };
	    mFVertexBuffer = FloatBuffer.wrap(coords);
	    mTexBuffer = FloatBuffer.wrap(tcoords);
	    mIndexBuffer = ShortBuffer.wrap(icoords);
    }

    public void draw(GL10 gl) {
        gl.glBindTexture(GL10.GL_TEXTURE_2D, playerTextures[0]);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mFVertexBuffer);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glEnable(GL10.GL_BLEND);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexBuffer);
        gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, VERTS,
                GL10.GL_UNSIGNED_SHORT, mIndexBuffer);
        gl.glDisable(GL10.GL_BLEND);
    }

	private void LoadTexture(GL10 gl, Context context)
	{
		int TEX_SIZE = 64;
        gl.glGenTextures(1, playerTextures, 0);
        Log.d("TextureID","Texture ID: "+playerTextures[0]);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, playerTextures[0]);

        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), mTextureResource);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();
//		int pixels[]=new int[TEX_SIZE*TEX_SIZE];
//		bmp.getPixels(pixels, 0, TEX_SIZE, 0, 0, TEX_SIZE, TEX_SIZE);
//		int pix1[]=new int[TEX_SIZE*TEX_SIZE];
//		for(int i=0; i<TEX_SIZE; i++)
//        {
//             for(int j=0; j<TEX_SIZE; j++)
//             {
//                  //correction of R and B
//                  int pix=pixels[i*TEX_SIZE+j];
//                  int pb=(pix>>16)&0xff;
//                  int pr=(pix<<16)&0x00ff0000;
//                  int px1=(pix&0xff00ff00) | pr | pb;
//                  //correction of rows
//                  pix1[(TEX_SIZE-i-1)*TEX_SIZE+j]=px1;
//             }
//        }     
//		
//		IntBuffer tbuf=IntBuffer.wrap(pix1);
//		tbuf.position(0);		
//		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, TEX_SIZE, TEX_SIZE, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, tbuf);		
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );
	}

    public void init(GL10 gl, Context mContext) {

    	LoadTexture(gl, mContext);
//		Bitmap bitmap;
//    	bitmap = BitmapFactory.decodeResource(mContext.getResources(), mTextureResource);
//        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//        bitmap.recycle();
	}
}
