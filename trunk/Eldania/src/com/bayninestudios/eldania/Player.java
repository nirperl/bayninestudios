package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.view.KeyEvent;

public class Player
{
	private DrawModel playerModel;
	public float facing = 0f;
	public Vector2 position;
	public float x,y,z;
	public float dx,dy;
	public boolean inCombat = false;
	private float MOVESPEED = 0.03f;
	
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

	public void setFacing()
	{
		if ((dx > 0) && (dy == 0))
			facing = 90f;
		else if ((dx < 0) && (dy == 0))
			facing = -90f;
		else if ((dx == 0) && (dy < 0))
			facing = 0f;
		else if ((dx == 0) && (dy > 0))
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

	public void moveCharacter(int keyCode, boolean keyUp)
	{
		if (!inCombat)
		{
			float newMove = MOVESPEED;
			if (keyUp)
				newMove = MOVESPEED * -1f;
			if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
			{
				dx = dx - newMove;
			}
			if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
			{
				dx = dx + newMove;
			}
			if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
			{
				dy = dy + newMove;
			}
			if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
			{
				dy = dy - newMove;
			}
			setFacing();
		}
		else
		{
			dx = 0;
			dy = 0;
		}
	}

	public void update(Landscape landscape)
	{
        float newCharX = x + dx;
        float newCharY = y + dy;

        if (landscape.checkPassable(newCharX, y))
        {
        	x = newCharX;
        }
        if (landscape.checkPassable(x, newCharY))
        {
        	y = newCharY;
        }
	}
}
