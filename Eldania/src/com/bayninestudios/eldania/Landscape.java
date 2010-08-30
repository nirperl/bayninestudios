package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class Landscape
{
    private DrawModel tile;
    private DrawModel dirtTile;
    private DrawModel grassTile;
    private DrawModel waterTile;
    private DrawModel beachTile;
    private DrawModel sandTile;
    private TileMap map;
    public boolean useTextures;
    private int blendFactor = GL10.GL_ONE_MINUS_SRC_ALPHA;

    private DrawModel fog;

    public Landscape(Context context)
    {
        tile = new DrawModel(context, R.xml.tile);
        dirtTile = new DrawModel(context, R.xml.tile);
        grassTile = new DrawModel(context, R.xml.tile);
        waterTile = new DrawModel(context, R.xml.tile);
        beachTile = new DrawModel(context, R.xml.tile);
        sandTile = new DrawModel(context, R.xml.tile);
        map = new TileMap(context, R.xml.map);
        fog = new DrawModel(context, R.xml.tile);
        useTextures = true;
    }

    public void loadTextures(GL10 gl, Context context)
    {
        dirtTile.loadTexture(gl, context, R.drawable.dirt12);
        grassTile.loadTexture(gl, context, R.drawable.acharya_grass3);
        waterTile.loadTexture(gl, context, R.drawable.water2);
        beachTile.loadTexture(gl, context, R.drawable.beach);
        sandTile.loadTexture(gl, context, R.drawable.sand);
        fog.loadTexture(gl, context, R.drawable.fog);
    }

    public void nextBlend()
    {
        int newBlend = GL10.GL_ZERO;
        switch (blendFactor)
        {
        case GL10.GL_ZERO:                  newBlend = GL10.GL_ONE;break;
        case GL10.GL_ONE:                   newBlend = GL10.GL_SRC_COLOR;break;
        case GL10.GL_SRC_COLOR:             newBlend = GL10.GL_ONE_MINUS_SRC_COLOR;break;
        case GL10.GL_ONE_MINUS_SRC_COLOR:   newBlend = GL10.GL_SRC_ALPHA;break;
        case GL10.GL_SRC_ALPHA:             newBlend = GL10.GL_ONE_MINUS_SRC_ALPHA;break;
        case GL10.GL_ONE_MINUS_SRC_ALPHA:   newBlend = GL10.GL_DST_ALPHA;break;
        case GL10.GL_DST_ALPHA:             newBlend = GL10.GL_ONE_MINUS_DST_ALPHA;break;
        case GL10.GL_ONE_MINUS_DST_ALPHA:   newBlend = GL10.GL_ZERO;break;
        default: break;
        }
        blendFactor = newBlend;
        Log.d(">", "new blend");

    }

    public void draw(GL10 gl, float charX, float charY)
    {
        gl.glColor4f(.5f, .5f, .5f, 1f);
        for (int y = -3; y < 4; y++)
        {
            for (int x = -4; x < 5; x++)
            {
                int tileX = x + (int) charX;
                int tileY = y + (int) charY;
                int tileType = map.getTile(tileX, tileY);
                if (tileType == 0)
                {
                    gl.glColor4f(0f, 0f, .7f, 1f);
                    if (useTextures)
                        waterTile.draw(gl, tileX, tileY, 0f);
                    else
                        tile.draw(gl, tileX, tileY, 0f);
                }
                else if (tileType == 1)
                {
                    gl.glColor4f(0f, 0.5f, 0f, 1f);
                    if (useTextures)
                        grassTile.draw(gl, tileX, tileY, 0f);
                    else
                        tile.draw(gl, tileX, tileY, 0f);
                }
                else if (tileType == 2)
                {
                    gl.glColor4f(0.5f, 0.25f, 0.25f, 1f);
                    if (useTextures)
                        dirtTile.draw(gl, tileX, tileY, 0f);
                    else
                        tile.draw(gl, tileX, tileY, 0f);
                }
                else if (tileType == 3)
                {
                    gl.glColor4f(0.5f, 0.25f, 0.25f, 1f);
                    if (useTextures)
                        beachTile.draw(gl, tileX, tileY, 0f);
                    else
                        tile.draw(gl, tileX, tileY, 0f);
                }
                else if (tileType == 4)
                {
                    gl.glColor4f(0.5f, 0.25f, 0.25f, 1f);
                    if (useTextures)
                        sandTile.draw(gl, tileX, tileY, 0f);
                    else
                        tile.draw(gl, tileX, tileY, 0f);
                }
            }
        }
    }
    
    // TODO: just a proof of concept
    public void drawFog(GL10 gl)
    {
        gl.glEnable(GL10.GL_BLEND);
//        gl.glDisable(GL10.GL_ALPHA_TEST);
        Vector3 scaleVec = new Vector3(10f,10f,1f);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, blendFactor);
        fog.draw(gl, 47f, 8f, 0.2f, 0f, scaleVec);
    }

    public boolean checkPassable(float x, float y)
    {
        return (map.checkPassible((int) x, (int) y));
    }
    
}
