package com.bayninestudios.eldania;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class Landscape
{
    private DrawModel tile;
    private DrawModel hwall;
    private DrawModel vwall;
    private TileMap map;
    public Cave cave;
    private DrawModel tombstone;
    private DrawModel tombstone2;
    public boolean useTextures;
    private int blendFactor = GL10.GL_ONE_MINUS_SRC_ALPHA;
    public ParticleSystem part;

    private DrawModel fog;
    private DrawModel fog2;
    
    public Landscape(Context context)
    {
        tile = new DrawModel(context, R.xml.tile);
        hwall = new DrawModel(context, R.xml.hwall);
        vwall = new DrawModel(context, R.xml.vwall);
        cave = new Cave(context);
        tombstone = new DrawModel(context, R.xml.tombstone1);
        tombstone2 = new DrawModel(context, R.xml.tombstone2);
        map = new TileMap(context, R.xml.map);
        fog = new DrawModel(context, R.xml.tile);
        fog2 = new DrawModel(context, R.xml.tile);
        useTextures = true;
        part = new ParticleSystem();
    }

    public void loadTextures(GL10 gl, Context context)
    {
        part.init(gl);
        tile.loadTexture(gl, context, R.drawable.supertexture);
        tile.superTexture();
        hwall.loadTexture(gl, context, R.drawable.supertexture);
        hwall.superTexture();
        vwall.loadTexture(gl, context, R.drawable.supertexture);
        vwall.superTexture();
        cave.loadTextures(gl, context);
        fog.loadTexture(gl, context, R.drawable.fog2);
        fog2.loadTexture(gl, context, R.drawable.fog2);
        tombstone.loadTexture(gl, context, R.drawable.tombstone2);
        tombstone2.loadTexture(gl, context, R.drawable.tombstone);
        
        addGameObject(1, 48.5f, 10f, 0f);
        addGameObject(1, 46.9f, 11f, 0f);
        addGameObject(1, 47.8f, 11.1f, 0f);
        addGameObject(1, 46.2f, 10.4f, 0f);
        addGameObject(2, 47.0f, 9.9f, 0f);
        addGameObject(2, 47.7f, 10.2f, 0f);
        addGameObject(2, 46.0f, 11.4f, 0f);
        addGameObject(1, 47.6f, 9.2f, 0f);
        addGameObject(1, 48.5f, 12.0f, 0f);
    }

    private void addGameObject(int type, float x, float y, float z)
    {
        GameObject tomb3 = new GameObject(type, new Vector3(x, y, z));
        map.addGameObj(tomb3);
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
                TileDef tileD = map.getTileD(tileX, tileY);
                int tileType = map.getTile(tileX, tileY);
                if (tileD.look != 0)
                    tile.tileDraw(gl, tileX, tileY, 0, (tileType - 1) * 8);
                if (tileD.wallw != 0)
                {
                    vwall.tileDraw(gl, tileX, tileY, 0f, (tileD.wallw - 1) * 8);
                }
                if (tileD.walle != 0)
                {
                    vwall.tileDraw(gl, tileX+1, tileY, 0f, (tileD.walle - 1) * 8);
                }
                if (tileD.walln != 0)
                {
                    hwall.tileDraw(gl, tileX, tileY+1, 0f, (tileD.walln - 1) * 8);
                }
                if (tileD.walls != 0)
                {
                    hwall.tileDraw(gl, tileX, tileY, 0f, (tileD.walls - 1) * 8);
                }
                if (tileD.gameObj != null)
                {
                    Iterator<GameObject> iter = tileD.gameObj.iterator();
                    while (iter.hasNext())
                    {
                        GameObject current = iter.next();
                        if (current.type == 1)
                            tombstone.draw(gl, current.position.x, current.position.y, current.position.z);
                        else
                            tombstone2.draw(gl, current.position.x, current.position.y, current.position.z);
                    }
                }
            }
        }
    }
    
    // TODO: just a proof of concept
    public void drawFog(GL10 gl)
    {
        fog.animateTex(0.0004f);
        fog2.animateTex(-0.0002f);
        gl.glEnable(GL10.GL_BLEND);
//        gl.glDisable(GL10.GL_ALPHA_TEST);
        Vector3 scaleVec = new Vector3(4.0f,3.9f,1f);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, blendFactor);
        fog.draw(gl, 45f, 9f, 0.2f, 0f, scaleVec);
        fog2.draw(gl, 45f, 9f, 0.21f, 0f, scaleVec);

        scaleVec.setxyz(4f, 3f, 1f);
        fog.draw(gl, 55f, 21f, 0.2f, 0f, scaleVec);
        fog2.draw(gl, 55f, 21f, 0.21f, 0f, scaleVec);
        gl.glDisable(GL10.GL_BLEND);
    }

    // TODO: another proof of concept. This thing is turning
    // more into a tech demo than a game :(
    public void drawPart(float x, float y, GL10 gl)
    {
        gl.glPushMatrix();
        gl.glTranslatef(x, y, 0f);
        part.draw(gl);
        gl.glPopMatrix();
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
