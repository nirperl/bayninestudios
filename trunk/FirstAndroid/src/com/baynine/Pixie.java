package com.baynine;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Pixie extends Model {

	private float x = 0.0f;
	private float y = 0.0f;
	private float z = 1.0f;
	private Random gen;
	
	public Pixie(Context context, int xmlFile, int mType)
	{
		super(context, xmlFile, mType);
		gen = new Random(System.currentTimeMillis());
	}

	public void animate()
	{
		float dx = ((gen.nextInt(20)/20f)-.50f)/60f;
		float dy = ((gen.nextInt(20)/20f)-.50f)/60f;
		x = x + dx;
		y = y + dy;
	}

	public void draw(GL10 gl) {
//		float scale = 10f;
		gl.glPushMatrix();
		gl.glTranslatef(x, y, z);
//		gl.glRotatef(rot, 0f, 0f, 1f);
//		gl.glScalef(scale, scale, scale);
		super.draw(gl);
	    gl.glPopMatrix();
	}
}
