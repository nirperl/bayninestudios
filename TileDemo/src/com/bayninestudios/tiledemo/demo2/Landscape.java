package com.bayninestudios.tiledemo.demo2;

import javax.microedition.khronos.opengles.GL10;

import com.bayninestudios.tiledemo.R;

import android.content.Context;
import java.util.Random;

public class Landscape
{
    private DrawModel tile;
    private int[][] tiles;

    public Landscape(Context context)
    {
        tile = new DrawModel(context, R.xml.tile);
        
        // create tiles, completely random
        tiles = new int[100][100];
        Random rand = new Random(System.currentTimeMillis());
        for (int x = 0; x < 100; x++)
            for (int y = 0; y < 100; y++)
            {
                tiles[x][y] = rand.nextInt() % 5;
            }
    }

    public void draw(GL10 gl, float charX, float charY)
    {
        // just draw the 10x10 tiles around the character
        for (int y = -10; y < 10; y++)
        {
            for (int x = -10; x < 10; x++)
            {
                int tileX = x + (int) charX;
                int tileY = y + (int) charY;
                int tileType = tiles[tileX][tileY];
                switch (tileType)
                {
                    case 0: gl.glColor4f(0f, 0f, .7f, 1f); break;
                    case 1: gl.glColor4f(0f, 0.5f, 0f, 1f); break;
                    case 2: gl.glColor4f(0.5f, 0.25f, 0.25f, 1f); break;
                    case 3: gl.glColor4f(0.5f, 0.5f, 0.25f, 1f); break;
                    case 4: gl.glColor4f(0.5f, 0.5f, 0.5f, 1f); break;
                    default: break;
                }
                tile.draw(gl, tileX, tileY, 0f);
            }
        }
    }
}
