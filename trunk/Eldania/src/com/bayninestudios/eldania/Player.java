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
	private float MOVESPEED = 1.3f; // in tiles per second
	public int maxHealth = 30;
	public int curHealth = 10;
	private DrawModel healthBar;
	public long healProc = 0;  // in milliseconds
	
	private long lastUpdate;
	
	public Player(Context context)
	{
		playerModel = new DrawModel(context, R.xml.player);
		this.x = 50.5f;
		this.y = 10.5f;
		this.z = 0f;
		lastUpdate = System.currentTimeMillis();
		healthBar = new DrawModel(context, R.xml.tile);
	}

	public void loadTextures(GL10 gl, Context context)
	{
		playerModel.loadTexture(gl, context, R.drawable.sand);
	}

	public void draw(GL10 gl)
	{
        gl.glColor4f(.1f, .1f, 1f, 1f);
		playerModel.draw(gl,0f,0f,0f,facing);
	}

	public void drawDash(GL10 gl)
	{
		Vector3 barScale = new Vector3();
		barScale.x = 1f;
		barScale.y = 0.1f;
		barScale.z = 1f;
		gl.glColor4f(.2f, .2f, .2f, 1f);
		healthBar.draw(gl, -4.7f, 2.6f, 0f, 0f, barScale);

		barScale.x = ((float)curHealth)/maxHealth;
		barScale.y = 0.1f;
		barScale.z = 1f;
		gl.glColor4f(.8f, 0f, 0f, 1f);
		healthBar.draw(gl, -4.7f, 2.6f, 0f, 0f, barScale);
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
			float newMove = 1f;
			if (keyUp)
				newMove = newMove * -1f;
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
		long curTime = System.currentTimeMillis();
		long timeDif = curTime - lastUpdate;
		float frameRate = timeDif/1000f;
		float moveSpeed = MOVESPEED * frameRate;
		// move character
        float newCharX = x + dx*moveSpeed;
        float newCharY = y + dy*moveSpeed;

        if (landscape.checkPassable(newCharX, y))
        {
        	x = newCharX;
        }
        if (landscape.checkPassable(x, newCharY))
        {
        	y = newCharY;
        }
        
        if (!inCombat)
        {
	        // heal if needed
	        if (curHealth < maxHealth)
	        {
	        	healProc = healProc + timeDif;
	        	if (healProc > 1000)
	        	{
	        		curHealth++;
	        		healProc = healProc - 1000;
	        	}
	        }
        }
        lastUpdate = curTime;
	}
}
