package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Player
{
	private DrawModel playerModel;
	public float facing = 0f;
	public float x,y,z;
	public float dx,dy;
	
	public Player(Context context)
	{
		playerModel = new DrawModel(context, R.xml.player);
		this.x = 50.5f;
		this.y = 10.5f;
		this.z = 0f;
	}
	
	public void draw(GL10 gl)
	{
        gl.glColor4f(.1f, .1f, 1f, 1f);
		playerModel.draw(gl,0f,0f,0f,facing);
	}
	
	public void setFacing(float charDX, float charDY)
	{
		if ((charDX > 0) && (charDY == 0))
			facing = 90f;
		else if ((charDX < 0) && (charDY == 0))
			facing = -90f;
		else if ((charDX == 0) && (charDY < 0))
			facing = 0f;
		else if ((charDX == 0) && (charDY > 0))
			facing = 180f;
		// TODO: I need to do something with these diagonal moves
//		else if ((charDX > 0) && (charDY > 0))
//			facing = 135f;
//		else if ((charDX > 0) && (charDY < 0))
//			facing = 45f;
//		else if ((charDX < 0) && (charDY > 0))
//			facing = -135f;
//		else if ((charDX < 0) && (charDY < 0))
//			facing = -45f;
	}
}
