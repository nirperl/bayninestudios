package com.bayninestudios.eldania;

import java.util.Random;

import android.util.Log;

public class PatrolComponent
{
    Vector3 oldPos;
    float centerX, centerY, centerZ;
    Vector3 dest;
    float radiusX, radiusY, radiusZ;
    Vector3 patVec;

    public PatrolComponent(float x, float y)
    {
        centerX = x;
        centerY = y;
        centerZ = 0f;
        radiusX = 1.0f;
        radiusY = 1.0f;
        radiusZ = 0f;
        dest = new Vector3();
        patVec = new Vector3();
        oldPos = new Vector3(x, y, 0f);
    }

    public void newPatrolDestination(Vector3 newDest)
    {
        newPatrolDestination(newDest.x, newDest.y, newDest.z);
    }

    public void newPatrolDestination(float x, float y, float z)
    {
        oldPos.x = x;
        oldPos.y = y;
        oldPos.z = z;
        do
        {
            long time = System.currentTimeMillis();
            Random gen = new Random(time);
            dest.x = (gen.nextFloat() * radiusX * 2) - (radiusX) + centerX;
            dest.y = (gen.nextFloat() * radiusY * 2) - (radiusY) + centerY;
            dest.z = (gen.nextFloat() * radiusZ * 2) - (radiusZ) + centerZ;

            patVec.x = dest.x - oldPos.x;
            patVec.y = dest.y - oldPos.y;
            patVec.z = dest.z - oldPos.z;
        }
        while (patVec.length() < .5f);
        patVec.normalize();
    }

    public void setPatrolDest(float x, float y, float z)
    {
        dest.x = x;
        dest.y = y;
        dest.z = z;
    }

    public boolean isAtDestination(Vector3 pos)
    {
        float fudge = .2f;
        boolean returnVal = false;
        if ((pos.x > (dest.x - fudge) && (pos.x < (dest.x + fudge))))
        {
            if ((pos.y > (dest.y - fudge) && (pos.y < (dest.y + fudge))))
            {
                returnVal = true;
            }
        }
        return (returnVal);
    }

    public Vector3 getPatrolDest()
    {
        return dest;
    }

    public Vector3 getPatrolVector()
    {
        return patVec;
    }
}
