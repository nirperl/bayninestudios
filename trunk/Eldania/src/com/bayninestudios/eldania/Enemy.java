package com.bayninestudios.eldania;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;

public class Enemy
{
    private DrawModel playerModel;
    public int facing = 32;
    public Vector3 position;
    public float dx;
    public float dy;
    public boolean inCombat = false;
    private float MOVESPEED = .10f; // in tiles per second
    public int maxHealth = 30;
    public int curHealth = 30;
    private DrawModel healthBar;
    public long healProcTimer = 0; // in milliseconds
    private long walkFrameTimer = 0;
    private int walkFrame = 1;
    private int WALKFRAMESPEED = 500;
    private PatrolComponent patrol;
    public boolean dead = false;

    public int actionTimer = 0;
    public final int ACTIONINTERVAL = 3000;

    public int textureResource;

    private long lastUpdate;

    public Enemy(Context context)
    {
        playerModel = new DrawModel(context, R.xml.player);
        this.position = new Vector3();
        setLocation(50.5f, 11.5f, 0f);
        lastUpdate = System.currentTimeMillis();
        healthBar = new DrawModel(context, R.xml.tile);
    }

    public void loadTextures(GL10 gl, Context context)
    {
        if (textureResource == 0)
            playerModel.loadTexture(gl, context, R.drawable.orc);
        else
            playerModel.loadTexture(gl, context, textureResource);

        playerModel.specialTex();
    }

    public void setLocation(float x, float y, float z)
    {
        position.x = x;
        position.y = y;
        position.z = z;
        patrol = new PatrolComponent(position.x, position.y);
        patrol.newPatrolDestination(position.x, position.y, position.z);
    }

    public void setSpeed(float speed, int frameSpeed)
    {
        MOVESPEED = speed;
        WALKFRAMESPEED = frameSpeed;
    }

    public void draw(GL10 gl)
    {
        int frameFacing = facing;
        if (walkFrame == 0)
        {
            frameFacing = frameFacing - 32;
        }
        else if (walkFrame == 2)
        {
            frameFacing = frameFacing + 32;
        }
        playerModel.specialDraw(gl, frameFacing, position);
    }

    public void drawDash(GL10 gl)
    {
        Vector3 barScale = new Vector3(1f, 0.1f, 1f);
        gl.glColor4f(.2f, .2f, .2f, 1f);
        healthBar.draw(gl, 3.2f, 2.6f, 0f, 0f, barScale);

        barScale.setxyz(((float) curHealth) / maxHealth, 0.1f, 1f);
        gl.glColor4f(.8f, 0f, 0f, 1f);
        healthBar.draw(gl, 3.2f, 2.6f, 0.1f, 0f, barScale);
    }

    public void setFacing()
    {
        if ((dx > 0) && (dy == 0))
            facing = 40;
        else if ((dx < 0) && (dy == 0))
            facing = 56;
        else if ((dx == 0) && (dy < 0))
            facing = 48;
        else if ((dx == 0) && (dy > 0))
            facing = 32;
    }

    public void setFacing(Vector3 direction)
    {
        // TODO: there is an easier way and it's using tan or
        // some other trig, bah, not now
        float diff = direction.x / direction.y;
        if (diff >= 0f)
        {
            if (direction.x > 0)
            {
                if (diff > 1.0f)
                    facing = 40;
                else
                    facing = 32;
            }
            else
            {
                if (diff > 1.0f)
                    facing = 56;
                else
                    facing = 48;
            }
        }
        else
        {
            if (direction.x > 0)
            {
                if (diff < -1.0f)
                    facing = 40;
                else
                    facing = 48;
            }
            else
            {
                if (diff < -1.0f)
                    facing = 56;
                else
                    facing = 32;
            }
        }
    }

    public void update(Landscape landscape)
    {
        facing = 48;
        long curTime = System.currentTimeMillis();
        long timeDif = curTime - lastUpdate;
        float frameRate = timeDif / 1000f;
        if (!inCombat)
        {
            if (!patrol.isAtDestination(position))
            {
                Vector3 patrolVector = patrol.getPatrolVector();
                setFacing(patrolVector);
                float moveSpeed = MOVESPEED * frameRate;
                // move character
                float newCharX = position.x + patrolVector.x * moveSpeed;
                float newCharY = position.y + patrolVector.y * moveSpeed;

                if (landscape.checkPassable(newCharX, position.y))
                {
                    position.x = newCharX;
                }
                if (landscape.checkPassable(position.x, newCharY))
                {
                    position.y = newCharY;
                }

                walkFrameTimer = walkFrameTimer + timeDif;
                if (walkFrameTimer > WALKFRAMESPEED)
                {
                    walkFrameTimer = walkFrameTimer - WALKFRAMESPEED;
                    walkFrame++;
                    if (walkFrame > 3)
                        walkFrame = 0;
                }
            }
            else
            {
                patrol.newPatrolDestination(position);
            }
        }
        lastUpdate = curTime;
    }
}
