package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Player
{
	private DrawModel playerModel;
	
	public Player(Context context)
	{
		playerModel = new DrawModel(context, R.xml.player);
	}
	
	public void draw(GL10 gl)
	{
		playerModel.draw(gl);
	}
}
