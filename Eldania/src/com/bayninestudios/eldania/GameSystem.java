package com.bayninestudios.eldania;

import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;

public class GameSystem
{
    private CombatSystem combatSystem;
    private ArrayList<Enemy> enemies;
    public Player mPlayer;
    private Landscape mLandscape;
    private Context context;
    private DrawModel targetTile;
    private Alphabet alpha;
    private Speedo mSpeedo;
    private RippleEffect ripples1;
    private boolean showText = false;

    private DrawModel necro;
    private Vector3 necroPos;

    public GameSystem(Context context)
    {
        this.context = context;
        mPlayer = new Player(context);
        combatSystem = new CombatSystem(mPlayer);
        mLandscape = new Landscape(context);
        targetTile = new DrawModel(context, R.xml.tile);

        necro = new DrawModel(context, R.xml.player);
        necroPos = new Vector3(52f, 14.3f, 0f);

        enemies = new ArrayList<Enemy>();
        alpha = new Alphabet(context);
        mSpeedo = new Speedo();
        ripples1 = new RippleEffect(context);
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
    }

    public void init(GL10 gl)
    {
        mLandscape.loadTextures(gl, context);
        mPlayer.loadTextures(gl, context);
        targetTile.loadTexture(gl, context, R.drawable.target);
        alpha.loadTexture(gl, context, R.drawable.simplealpha);
        necro.loadTexture(gl, context, R.drawable.necromancer);
        ripples1.init(gl, context);
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext())
        {
            Enemy current = iter.next();
            current.loadTextures(gl, context);
        }
    }

    // there's a problem with this code, checkcombat will keep adding
    // the same monsters every update.  need to only add once. use an
    // in combat flag
    public void checkCombat()
    {
        Iterator<Enemy> iter = enemies.iterator();
        while (iter.hasNext())
        {
            Enemy current = iter.next();
            float aggro = 0.7f;
            Vector3 playVec = mPlayer.position;
            if (Util.isInBox(playVec, current.position, aggro))
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
        ripples1.update();
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

        necro.draw(gl, necroPos.x, necroPos.y, necroPos.z);

        if (combatSystem.combatActive)
        {
            drawTargetTile(gl, combatSystem.getTarget());
        }
        ripples1.draw(gl);
        mLandscape.drawPart(49.5f, 13.5f, gl);
        mLandscape.drawPart(51.5f, 13.5f, gl);

        gl.glPopMatrix();
        mPlayer.draw(gl);

        // TODO fix up, just a proof of concept, but not bad for a proof
        gl.glPushMatrix();
        gl.glTranslatef(-mPlayer.position.x, -mPlayer.position.y, -mPlayer.position.z);
        mLandscape.drawFog(gl);
        gl.glPopMatrix();
    }

    public void drawTargetTile(GL10 gl, Vector3 target)
    {
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
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
 
        if (showText)
            alpha.draw(gl, -4f, 2f, "Hi!\nCan you please kill five\nskeletons?\nThanks");
    }

    public void playerAction()
    {
        if (combatSystem.combatActive)
        {
            combatSystem.attack();
        }
        else
        {
            checkActionable(mPlayer.position, mPlayer.facing);
        }
    }

    private void checkActionable(Vector3 position, int facing)
    {
        float reach = 0.5f;
        Vector3 checkPoint = position.clone();
        switch (facing)
        {
            case 40: checkPoint.x += reach; break;
            case 56: checkPoint.x -= reach; break;
            case 32: checkPoint.y += reach; break;
            case 48: checkPoint.y -= reach; break;
            default: break;
        }
        if (Util.isInBox(position, necroPos, .7f, .4f))
        {
            if (showText)
            {
                mPlayer.talking = false;
                showText = false;
            }
            else
            {
                showText = true;
                mPlayer.talking = true;
            }
        }
    }

    public void playerAction2()
    {
    }
    
    public void startSpeedo()
    {
        mSpeedo.setStartTime();
    }
    
    public void stopSpeedo()
    {
        mSpeedo.setEndTime();
    }
    
    public void drawSpeedo(GL10 gl)
    {
        float rate = mSpeedo.getRate();
        alpha.draw(gl, 3.5f, -2.8f, Float.toString(rate));
    }
}
