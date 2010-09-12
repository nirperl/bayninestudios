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
    private DrawModel targetTile;
    private Alphabet alpha;

    public GameSystem(Context context)
    {
        this.context = context;
        mPlayer = new Player(context);
        combatSystem = new CombatSystem(mPlayer);
        mLandscape = new Landscape(context);
        house = new DrawModel(context, R.xml.house);
        tombstone = new DrawModel(context, R.xml.tombstone);
        targetTile = new DrawModel(context, R.xml.tile);
        enemies = new ArrayList<Enemy>();
        alpha = new Alphabet(context);
        addEnemies();
    }

    private void addEnemy(float x, float y, float s, int f, int tex)
    {
        Enemy enemy = new Enemy(context);
        enemy.textureResource = tex;
        enemy.setLocation(x, y, 0f);
        enemy.setSpeed(s, f);
        enemies.add(enemy);
    }

    public void addEnemies()
    {
        addEnemy(51.5f, 9f, .2f, 200, R.drawable.orc);
        addEnemy(49.5f, 20.5f, 0.15f, 450, R.drawable.skeleton);
        addEnemy(49.5f, 20.5f, 0.10f, 500, R.drawable.skeleton);
        addEnemy(57.5f, 22.5f, 0.10f, 500, R.drawable.skeleton);
        addEnemy(46.5f, 9.5f, .15f, 400, R.drawable.skeleton);
        //
        // addEnemy(50.5f, 11.5f, .2f, 300, R.drawable.orc);
        // addEnemy(51.5f, 10f, .2f, 350, R.drawable.orc);
//         addEnemy(48.5f, 10f, .1f, 500, R.drawable.skeleton);
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
        targetTile.loadTexture(gl, context, R.drawable.target);
        alpha.loadTexture(gl, context, R.drawable.alphabet);
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
            float aggro = 0.7f;
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
        mLandscape.updatePart();
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
//        house.draw(gl, 47f, 13f, 0f, 0f, 1.75f);
        tombstone.draw(gl, 48.5f, 10f, 0f);
        tombstone.draw(gl, 47.7f, 10.3f, 0f);
        tombstone.draw(gl, 47.0f, 9.9f, 0f);

        if (combatSystem.combatActive)
        {
            drawTargetTile(gl, combatSystem.getTarget());
        }
        mLandscape.drawPart(gl);

        gl.glPopMatrix();
        mPlayer.draw(gl);

        

        // TODO fix up, just a proof of concept
//        gl.glPushMatrix();
//        gl.glTranslatef(-mPlayer.x, -mPlayer.y, -mPlayer.z);
//        mLandscape.drawFog(gl);
    }

    public void drawTargetTile(GL10 gl, Vector3 target)
    {
        gl.glEnable(GL10.GL_BLEND);
        targetTile.draw(gl, target.x - 0.5f, target.y - 0.4f, target.z+0.01f);
        gl.glDisable(GL10.GL_BLEND);
    }

    public void drawDash(GL10 gl)
    {
        gl.glDisable(GL10.GL_TEXTURE_2D);
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
        gl.glEnable(GL10.GL_TEXTURE_2D);
        alpha.draw(gl, -4f, 1f, 6);
        alpha.draw(gl, -3.4f, 1f, 7);
        alpha.draw(gl, -2.8f, 1f, 8);
        alpha.draw(gl, -2.2f, 1f, 9);
        alpha.draw(gl, -1.6f, 1f, 10);
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
