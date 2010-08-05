package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Landscape {

	private DrawModel tile;
	private TileMap map;

	public Landscape(Context context)
	{
		tile = new DrawModel(context, R.xml.tile);
		map = new TileMap(context, R.xml.map);
	}

	public void draw(GL10 gl, float charX, float charY)
	{
		for (int y = -3; y < 4; y++)
		{
			for (int x = -4; x < 5; x++)
			{
				int tileX = x+(int)charX;
				int tileY = y+(int)charY;
				int tileType = map.getTile(tileX, tileY);
				if (tileType == 0)
				{
					gl.glColor4f(0f, 0f, 1f, 1f);
				}
				else if (tileType == 1)
				{
					gl.glColor4f(0f, 0.6f, 0f, 1f);
					
				}
				else if (tileType == 2)
				{
					gl.glColor4f(0.5f, 0.25f, 0.25f, 1f);
					
				}
				tile.draw(gl, tileX, tileY, 0f);
			}
		}
	}
	public boolean checkPassable(float x, float y)
	{
		return (map.checkPassible((int)x,(int)y));
	}
}
