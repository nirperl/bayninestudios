// /*
package com.baynine;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class Landscape {

//	private ArrayList<Tile> mTiles;
	private int[][] config = {
			{0,1,1,0,0,0,0,0,0,0},
			{0,1,1,0,0,0,0,0,0,0},
			{0,1,1,0,0,1,1,1,1,0},
			{0,1,1,0,1,1,0,0,1,0},
			{0,1,1,0,1,0,0,0,1,0},
			{0,1,1,1,1,1,1,1,1,0},
			{0,0,0,0,0,0,0,0,0,0},
			{2,2,2,2,2,2,2,2,2,2},
			{2,2,2,2,2,2,2,2,2,2},
			{2,2,2,2,2,2,2,2,2,2}};

	private Tile grassTile;
	private Tile dirtTile;
	private Tile waterTile;
	private Post mPost;
	private Model mDiamond;
	private float rot = 5f;
//	private int[] tileTextures = new int[1];
//	private int mTextureResource;

	public Landscape(Context context) {
//		mTiles = new ArrayList<Tile>();
//		mTiles.add(new Tile(R.drawable.grass_sm));
//		mTiles.add(new Tile(R.drawable.dirt12));
		grassTile = new Tile(R.drawable.grass_sm);
		dirtTile = new Tile(R.drawable.dirt12);
		waterTile = new Tile(R.drawable.water);
		mPost = new Post(R.drawable.post);
		mDiamond = new Model(context, R.xml.sphere,1);
	}

	public void init(GL10 gl, Context mContext, int tex) {

        mPost.loadTexture(gl, mContext);
        mDiamond.loadTexture(gl, mContext, R.drawable.sphere);
        grassTile.loadTexture(gl, mContext);
        dirtTile.loadTexture(gl, mContext);
        waterTile.loadTexture(gl, mContext);
//		mTextureResource = tex;
//		gl.glGenTextures(1, tileTextures, 0);
//        gl.glBindTexture(GL10.GL_TEXTURE_2D, tileTextures[0]);
//
//		Bitmap bitmap;
//    	bitmap = BitmapFactory.decodeResource(mContext.getResources(), mTextureResource);
//        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
//        bitmap.recycle();
	}

	public void draw(GL10 gl)
	{
		grassTile.bindTex(gl);
		for (int x=-3; x<3; x++) {
			for (int y=-4; y<4; y++) {
			    if (config[x+3][y+4] == 0)
			    {
			    	grassTile.draw(x, y, gl);
			    }
			}
		}
		dirtTile.bindTex(gl);
		for (int x=-3; x<3; x++) {
			for (int y=-4; y<4; y++) {
			    if (config[x+3][y+4] == 1)
			    {
			    	dirtTile.draw(x, y, gl);
			    }
			}
		}
		waterTile.bindTex(gl);
		for (int x=-5; x<5; x++) {
			for (int y=-5; y<5; y++) {
			    if (config[x+5][y+5] == 2)
			    {
			    	waterTile.draw(x, y, gl);
			    }
			}
		}
//		for (int x=-5; x<5; x++) {
//			for (int y=-5; y<5; y++) {
//			    if (config[x+5][y+5] == 0)
//			    {
//					grassTile.bindTex(gl);
//			    	grassTile.draw(x, y, gl);
//			    }
//			    else
//			    {
//					dirtTile.bindTex(gl);
//			    	dirtTile.draw(x, y, gl);
//			    }
//			}
//		}
	}

	public void drawForeground(GL10 gl) {
		mPost.draw(gl, -4.0f, -1.0f, 0.0f);
		mPost.draw(gl, -3.0f, -1.0f, 0.0f);
		mPost.draw(gl, -2.0f, -1.0f, 0.0f);
		mPost.draw(gl, -1.0f, -1.0f, 0.0f);
		mPost.draw(gl, 0.0f, -1.0f, 0.0f);
		mPost.draw(gl, 1.0f, -1.0f, 0.0f);
		mPost.draw(gl, 2.0f, -1.0f, 0.0f);
//		mPost.draw(gl, 3.0f, -1.0f, 0.0f, rot++);
		mPost.draw(gl, 3.0f, -1.0f, 0.0f);
		mDiamond.draw(gl, -1f, 2f, 2.5f, rot++);
	}
}
// */
/*
package com.baynine;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Landscape {

//	private ArrayList<Tile> mTiles;
	private NewTile[] tiles;
	private int[][] config = {
			{0,1,1,0,0,0,0,0,0,0},
			{0,1,1,0,0,0,0,0,0,0},
			{0,1,1,0,0,1,1,1,1,0},
			{0,1,1,0,1,1,0,0,1,0},
			{0,1,1,0,1,0,0,0,1,0},
			{0,1,1,1,1,1,1,1,1,0},
			{0,0,0,0,0,0,0,0,0,0},
			{2,2,2,2,2,2,2,2,2,2},
			{2,2,2,2,2,2,2,2,2,2},
			{2,2,2,2,2,2,2,2,2,2}};

	private Plant mPlant;
	private Post mPost;
	private Diamond mDiamond;
	private float rot = 5f;

	public Landscape() {
		tiles = new NewTile[3];
		for (int i = 0; i < 3; i++) {
			tiles[i] = new NewTile(R.drawable.grass_sm);
		}
		mPlant = new Plant(-1,-1, R.drawable.plant);
		mPost = new Post(R.drawable.post);
		mDiamond = new Diamond();
	}

	private int getTileResource(int i) {
		int returnRes = R.drawable.grass_sm;
		if (i == 1) {
			returnRes = R.drawable.dirt12;
		} else if (i == 2) {
			returnRes = R.drawable.water;
		}
		return returnRes;
	}

	public void init(GL10 gl, Context mContext) {
		for (int i = 0; i < 3; i++) {
			tiles[i].loadTexture(gl, mContext);
		}
		mPlant.loadTexture(gl, mContext);
		mPost.loadTexture(gl, mContext);
	}

	public void draw(GL10 gl) {
		for (int x=-5; x<5; x++) {
			for (int y=4; y>-5; y--) {
			    gl.glPushMatrix();
			    gl.glTranslatef((float)x, (float)y, 0.0f);
				tiles[0].draw(x, y, gl);
			    gl.glPopMatrix();
			}
		}
	}

	public void drawForeground(GL10 gl) {
		mPost.draw(gl, -2.0f, -1.0f, 0.0f);
		mPost.draw(gl, 0.5f, -1.0f, 0.0f);
		mPost.draw(gl, 3.0f, -1.0f, 0.0f, rot++);
		mDiamond.draw(gl, -1f, 2f, 2f);
		mPlant.draw(gl);
	}
}

*/
