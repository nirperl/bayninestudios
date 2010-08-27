package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class GameSystem
{
    private CombatSystem combatSystem;
    private DrawModel house;
    private DrawModel tombstone;

	public GameSystem(Context context)
	{
		house = new DrawModel(context, R.xml.house);
		tombstone = new DrawModel(context, R.xml.tombstone);
	}

	public void init(GL10 gl, Context context)
	{
        house.loadTexture(gl, context, R.drawable.stone);
        tombstone.loadTexture(gl, context, R.drawable.tombstone2);
	}

	public void update()
	{
	}

	public void draw(GL10 gl)
	{
        house.draw(gl, 47f, 13f, 0f, 0f, 1.75f);
        gl.glColor4f(.5f, .5f, .5f, 1f);
        tombstone.draw(gl, 48.5f, 10f, 0f);
        tombstone.draw(gl, 47.7f, 10.3f, 0f);
        tombstone.draw(gl, 47.0f, 9.9f, 0f);
	}
}
