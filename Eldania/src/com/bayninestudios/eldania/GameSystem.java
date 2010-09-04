package com.bayninestudios.eldania;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class GameSystem
{
    private CombatSystem combatSystem;
    private DrawModel house;
    private DrawModel tombstone;
    private ArrayList<Enemy> enemies;
    public Player mPlayer;
    private Landscape mLandscape;
    private Context context;
    private DrawModel waterTile;

    public GameSystem(Context context)
    {
        this.context = context;
        mPlayer = new Player(context);
        combatSystem = new CombatSystem(mPlayer);
        mLandscape = new Landscape(context);
        house = new DrawModel(context, R.xml.house);
        tombstone = new DrawModel(context, R.xml.tombstone);
        waterTile = new DrawModel(context, R.xml.tile);
        enemies = new ArrayList<Enemy>();
        addEnemies();
    }

    private void addEnemy(float x, float y, float s, int f, int tex)
    {
        Enemy orc = new Enemy(context);
        orc.textureResource = tex;
        orc.setLocation(x, y, 0f);
        orc.setSpeed(s, f);
        enemies.add(orc);
    }

    public void addEnemies()
    {
        addEnemy(50.5f, 11.5f, .2f, 300, R.drawable.demon);
        addEnemy(51.5f, 9f, .2f, 200, R.drawable.orc);
        addEnemy(47.5f, 10f, .1f, 450, R.drawable.skeleton);
        // addEnemy(46.5f, 9.5f, .15f, 400, R.drawable.skeleton);
        //
        // addEnemy(50.5f, 11.5f, .2f, 300, R.drawable.orc);
        // addEnemy(51.5f, 10f, .2f, 350, R.drawable.orc);
        // addEnemy(48.5f, 10f, .1f, 500, R.drawable.skeleton);
        // addEnemy(46.5f, 9.5f, .2f, 350, R.drawable.skeleton);
    }

    public void playerAction()
    {
        if (combatSystem.combatActive)
        {
            combatSystem.attack();
        }
    }

    public void init(GL10 gl)
    {
        mLandscape.loadTextures(gl, context);
        mPlayer.loadTextures(gl, context);
        house.loadTexture(gl, context, R.drawable.stone);
        tombstone.loadTexture(gl, context, R.drawable.tombstone2);
        waterTile.loadTexture(gl, context, R.drawable.target);
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext())
        {
            Enemy current = iter.next();
            current.loadTextures(gl, context);
        }
    }

    public boolean isInBox(Vector3 pos1, Vector3 pos2, float rad)
    {
        boolean returnVal = false;
        if ((pos1.x > (pos2.x - rad) && (pos1.x < (pos2.x + rad))))
        {
            if ((pos1.y > (pos2.y - rad) && (pos1.y < (pos2.y + rad))))
            {
                returnVal = true;
            }
        }
        return (returnVal);
    }

    public void checkCombat()
    {
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext())
        {
            Enemy current = iter.next();
            float aggro = 0.5f;
            Vector3 playVec = mPlayer.position;
            if (isInBox(playVec, current.position, aggro))
            {
                combatSystem.addEnemy(current);
            }
        }
    }

    public void update()
    {
        mPlayer.update(mLandscape);
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext())
        {
            Enemy current = iter.next();
            if (current.dead)
                iter.remove();
            current.update(mLandscape);
        }
        checkCombat();
        combatSystem.update();
    }

    public void draw(GL10 gl)
    {
        gl.glPushMatrix();
        gl.glTranslatef(-mPlayer.position.x, -mPlayer.position.y, -mPlayer.position.z);
        mLandscape.draw(gl, mPlayer.position.x, mPlayer.position.y);

        // draw sprites
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext())
        {
            Enemy current = iter.next();
            current.draw(gl);
        }
        house.draw(gl, 47f, 13f, 0f, 0f, 1.75f);
        tombstone.draw(gl, 48.5f, 10f, 0f);
        tombstone.draw(gl, 47.7f, 10.3f, 0f);
        tombstone.draw(gl, 47.0f, 9.9f, 0f);

        if (combatSystem.combatActive)
        {
            Log.d("target",combatSystem.getTarget().toString());
            drawTargetTile(gl, combatSystem.getTarget());
        }

        gl.glPopMatrix();
        mPlayer.draw(gl);

        // TODO fix up, just a proof of concept
//        gl.glPushMatrix();
//        gl.glTranslatef(-mPlayer.x, -mPlayer.y, -mPlayer.z);
//        mLandscape.drawFog(gl);
//        gl.glPopMatrix();
    }
    public void drawTargetTile(GL10 gl, Vector3 target)
    {
        waterTile.draw(gl, target.x - 0.5f, target.y - 0.4f, target.z+0.01f);
    }

    public void drawDash(GL10 gl)
    {
        Iterator<Enemy> iter = enemies.iterator();
        mPlayer.drawDash(gl);
        while (iter.hasNext())
        {
            Enemy current = iter.next();
            if (current.dead)
                iter.remove();
            if (current.inCombat)
                current.drawDash(gl);
        }
    }

    public void toggleTextures()
    {
//        if (mLandscape.useTextures)
//            mLandscape.useTextures = false;
//        else
//            mLandscape.useTextures = true;
//        mLandscape.nextBlend();
        
    }
}
