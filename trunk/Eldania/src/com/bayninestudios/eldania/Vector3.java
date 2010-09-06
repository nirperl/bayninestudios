package com.bayninestudios.eldania;

public class Vector3
{
    public float x;
    public float y;
    public float z;

    public Vector3()
    {
        this(0f, 0f, 0f);
    }

    public Vector3(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setxyz(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;        
    }

    public final float length()
    {
        return (float) Math.sqrt((x * x) + (y * y) + (z * z));
    }

    public final float normalize()
    {
        final float magnitude = length();

        if (magnitude != 0.0f)
        {
            x /= magnitude;
            y /= magnitude;
        }

        return magnitude;
    }

    public final void subtract(Vector3 other)
    {
        x -= other.x;
        y -= other.y;
        z -= other.z;
    }

    public final void multiply(float factor)
    {
        x *= factor;
        y *= factor;
        z *= factor;
    }

    public String toString()
    {
        return (new String(x + "," + y + "," + "," + z));
    }
}
