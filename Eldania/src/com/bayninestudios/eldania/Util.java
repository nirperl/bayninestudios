package com.bayninestudios.eldania;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

public class Util
{
    static Random gen = new Random(System.currentTimeMillis());;
    static public float randomFloat()
    {
//        if (gen == null)
//            gen = new Random(System.currentTimeMillis());
        return gen.nextFloat();
    }

    // used to make native order float buffers
    static public FloatBuffer makeFloatBuffer(float[] arr)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(arr);
        fb.position(0);
        return fb;
    }

    // used to make native order short buffers
    static public ShortBuffer makeShortBuffer(short[] arr)
    {
        ByteBuffer bb = ByteBuffer.allocateDirect(arr.length * 4);
        bb.order(ByteOrder.nativeOrder());
        ShortBuffer ib = bb.asShortBuffer();
        ib.put(arr);
        ib.position(0);
        return ib;
    }

    /**
     * Checks to see if a point is inside a box
     * @param pos1 - the center of the box to check
     * @param pos2 - the point to check to see if it is inside the box
     * @param rad - the radius of the box to check
     * @return
     */
    static public boolean isInBox(Vector3 pos1, Vector3 pos2, float radx, float rady)
    {
        boolean returnVal = false;
        if ((pos1.x > (pos2.x - radx) && (pos1.x < (pos2.x + radx))))
        {
            if ((pos1.y > (pos2.y - rady) && (pos1.y < (pos2.y + rady))))
            {
                returnVal = true;
            }
        }
        return (returnVal);
    }

    static public boolean isInBox(Vector3 pos1, Vector3 pos2, float rad)
    {
        return isInBox(pos1, pos2, rad, rad);
    }
}
