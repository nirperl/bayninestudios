package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class Landscape
{
    private DrawModel tile;
    private TileMap map;
    public Cave cave;
    public boolean useTextures;
    private int blendFactor = GL10.GL_ONE_MINUS_SRC_ALPHA;
    private ParticleSystem part;

    private DrawModel fog;
    
    public Landscape(Context context)
    {
        tile = new DrawModel(context, R.xml.tile);
        cave = new Cave(context);
        map = new TileMap(context, R.xml.map);
        fog = new DrawModel(context, R.xml.tile);
        useTextures = true;
        part = new ParticleSystem();
    }

    public void loadTextures(GL10 gl, Context context)
    {
        tile.loadTexture(gl, context, R.drawable.supertexture);
        tile.superTexture();
        cave.loadTextures(gl, context);
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
        for (int y = -3; y < 4; y++)
        {
            for (int x = -4; x < 5; x++)
            {
                int tileX = x + (int) charX;
                int tileY = y + (int) charY;
                int tileType = map.getTile(tileX, tileY);
                if (tileType != 0)
                    tile.tileDraw(gl, tileX, tileY, 0, (tileType - 1) * 8);
            }
        }
        cave.draw(gl);
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

    public void drawPart(GL10 gl)
    {
        part.draw(gl);
    }

    public void updatePart()
    {
        part.update();
    }

    public boolean checkPassable(float x, float y)
    {
        return (map.checkPassible((int) x, (int) y));
    }
    
}
