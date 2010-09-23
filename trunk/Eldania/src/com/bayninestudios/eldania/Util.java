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

}
